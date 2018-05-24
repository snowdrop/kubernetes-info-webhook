package me.snowdrop.kubernetes.info.webhook

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import me.snowdrop.kubernetes.info.webhook.model.AdmissionResponse
import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview
import me.snowdrop.kubernetes.info.webhook.util.DeepClone
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import static io.fabric8.zjsonpatch.JsonDiff.asJson

@RestController
@Slf4j
class MutatingController {

    private final Mutator mutator
    private final ObjectMapper objectMapper

    MutatingController(Mutator mutator, ObjectMapper objectMapper) {
        this.mutator = mutator
        this.objectMapper = objectMapper
    }

    @PostMapping("/mutate")
    AdmissionReview mutate(@RequestBody final AdmissionReview input) {
        log.debug("Received input:\n{}", input)

        final originalObject = input.request.object
        final admissionResponse = AdmissionResponse.withUId(input.request.uid)
        input.response = admissionResponse

        final Mutator.Response mutationResponse = mutator.mutate(DeepClone.deepClone(originalObject), input)
        if (mutationResponse.mutated) {
            admissionResponse.patch = objectMapper.writeValueAsBytes(
                    asJson(toJsonNode(originalObject), toJsonNode(mutationResponse.newObject)))
        }

        log.debug("Adding the following response to AdmissionReview:\n{}", input.response)

        return input
    }

    private JsonNode toJsonNode(Object o) {
        objectMapper.convertValue(o, JsonNode.class)
    }

}
