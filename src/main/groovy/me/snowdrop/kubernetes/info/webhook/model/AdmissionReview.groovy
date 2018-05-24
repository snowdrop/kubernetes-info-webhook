package me.snowdrop.kubernetes.info.webhook.model

import groovy.transform.ToString

@ToString(includePackage = false)
class AdmissionReview {

    String kind = "AdmissionReview"
    String apiVersion
    AdmissionRequest request
    AdmissionResponse response
}
