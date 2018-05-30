package me.snowdrop.kubernetes.info.webhook

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileDynamic
import groovy.util.logging.Slf4j
import me.snowdrop.kubernetes.info.webhook.Mutator.NoOpMutator
import me.snowdrop.kubernetes.info.webhook.match.MutateObjectPredicate
import me.snowdrop.kubernetes.info.webhook.model.AdmissionResponse
import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview
import me.snowdrop.kubernetes.info.webhook.util.DeepClone
import org.springframework.stereotype.Component

import static io.fabric8.zjsonpatch.JsonDiff.asJson

@Component
@Slf4j
class DefaultMutatingService implements MutatingService {

    private final KubernetesInfoProperties properties
    private final Mutator mutator
    private final ObjectMapper objectMapper
    private final MutateObjectPredicate predicate

    private final NoOpMutator noOpMutator = new NoOpMutator()

    DefaultMutatingService(KubernetesInfoProperties properties, Mutator mutator,
                           MutateObjectPredicate predicate, ObjectMapper objectMapper) {
        this.properties = properties
        this.mutator = mutator
        this.predicate = predicate
        this.objectMapper = objectMapper
    }

    @Override
    AdmissionReview mutate(AdmissionReview input) {
        final originalObject = input.request.object
        final admissionResponse = AdmissionResponse.withUId(input.request.uid)
        input.response = admissionResponse

        final Mutator.Response mutationResponse =
                getMutator(originalObject).mutate(DeepClone.deepClone(originalObject), input)
        if (mutationResponse.mutated) {
            admissionResponse.patch = objectMapper.writeValueAsBytes(
                    asJson(toJsonNode(originalObject), toJsonNode(mutationResponse.newObject)))
        }

        return input
    }

    private JsonNode toJsonNode(Object o) {
        objectMapper.convertValue(o, JsonNode.class)
    }

    private Mutator getMutator(Map<String, Object> originalObject) {
        final annotations = getObjectAnnotations(originalObject)
        if(predicate.test(
                new MutateObjectPredicate.Input(
                        properties.policy,
                        properties.matchingAnnotation,
                        annotations))) {
            return mutator
        }

        log.debug("Input object with annotations {} is not eligible for mutation under policy {} and annotation {}",
                annotations, properties.policy, properties.matchingAnnotation)
        return noOpMutator
    }

    @CompileDynamic
    private Map<String, String> getObjectAnnotations(Map<String, Object> object) {
        return object.metadata.annotations
    }
}
