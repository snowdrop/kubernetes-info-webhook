package me.snowdrop.kubernetes.info.webhook

import com.fasterxml.jackson.databind.ObjectMapper
import io.fabric8.kubernetes.client.KubernetesClient
import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview
import me.snowdrop.kubernetes.info.webhook.script.ScriptRunner

class ScriptRunnerMutator implements Mutator {

    private final ObjectMapper objectMapper
    private final KubernetesClient kubernetesClient
    private final ScriptRunner scriptRunner

    ScriptRunnerMutator(ObjectMapper objectMapper, KubernetesClient kubernetesClient,
                        ScriptRunner scriptRunner) {
        this.objectMapper = objectMapper
        this.kubernetesClient = kubernetesClient
        this.scriptRunner = scriptRunner
    }

    @Override
    Response mutate(Map<String, Object> originalObjectCopy, AdmissionReview admissionReview) {
        return Response.mutated(scriptRunner.run(
            new ScriptRunner.Input(originalObjectCopy, admissionReview,
                    kubernetesClient, objectMapper)
        ))
    }
}
