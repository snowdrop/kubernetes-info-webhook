package me.snowdrop.kubernetes.info.webhook.model

import groovy.transform.ToString

@ToString(includePackage = false)
class Status {

    enum Type {
        Success, Failure
    }

    Type status
    String message
    Integer code
}
