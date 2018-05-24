package me.snowdrop.kubernetes.info.webhook.script

import com.fasterxml.jackson.databind.ObjectMapper
import io.fabric8.kubernetes.client.KubernetesClient
import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview

interface ScriptRunner {

    Map<String, Object> run(Input input)

    class Input {

        final Map<String, Object> originalObjectCopy
        final AdmissionReview admissionReview
        final KubernetesClient kubernetesClient
        final ObjectMapper objectMapper

        Input(Map<String, Object> originalObjectCopy, AdmissionReview admissionReview,
              KubernetesClient kubernetesClient, ObjectMapper objectMapper) {
            this.originalObjectCopy = originalObjectCopy
            this.admissionReview = admissionReview
            this.kubernetesClient = kubernetesClient
            this.objectMapper = objectMapper
        }
    }
}
