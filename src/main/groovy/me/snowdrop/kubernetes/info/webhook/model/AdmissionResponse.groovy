package me.snowdrop.kubernetes.info.webhook.model

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.ToString

@ToString(includePackage = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
class AdmissionResponse {

    String uid
    boolean allowed = true
    //when not allowed, gives the reason why it was not allowed
    Status status
    byte[] patch
    String patchType = "JSONPatch"

    static AdmissionResponse withUId(String uid) {
        final result = new AdmissionResponse()
        result.uid = uid
        return result
    }
}
