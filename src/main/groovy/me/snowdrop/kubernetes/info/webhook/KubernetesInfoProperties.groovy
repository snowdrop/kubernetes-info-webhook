package me.snowdrop.kubernetes.info.webhook

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("k8s.info")
class KubernetesInfoProperties {

    boolean enabled = true
    MutatingScript script = new MutatingScript()

    class MutatingScript {
        String name = "mutator.groovy"

        /**
         *  When this is false the script will be read from the classpath
         */
        boolean loadFromConfigMap = true
        
        String configMapName = "k8s-info-mutating-script"
    }
}
