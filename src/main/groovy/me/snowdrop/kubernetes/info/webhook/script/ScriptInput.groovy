package me.snowdrop.kubernetes.info.webhook.script

import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview

class ScriptInput {

    final Map<String, Object> originalObjectCopy
    final AdmissionReview admissionReview

    ScriptInput(Map<String, Object> originalObjectCopy, AdmissionReview admissionReview) {
        this.originalObjectCopy = originalObjectCopy
        this.admissionReview = admissionReview
    }
}
