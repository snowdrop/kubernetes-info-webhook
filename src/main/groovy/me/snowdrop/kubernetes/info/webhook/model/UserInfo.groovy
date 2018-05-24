package me.snowdrop.kubernetes.info.webhook.model

import groovy.transform.ToString

@ToString(includePackage = false)
class UserInfo {

    String username
    List<String> groups
}
