package me.snowdrop.kubernetes.info.webhook.model

import groovy.transform.ToString

@ToString(includePackage = false)
class AdmissionRequest {

    String uid
    String namespace
    Operation operation
    UserInfo userInfo
    Map<String, Object> object
    Map<String, Object> oldObject
}
