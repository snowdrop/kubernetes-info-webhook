package me.snowdrop.kubernetes.info.webhook

import me.snowdrop.kubernetes.info.webhook.match.MatchPolicy
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("k8s.info")
class KubernetesInfoProperties {

    /**
     * If disabled, incoming requests will not be mutated all
     */
    boolean enabled = true

    /**
     * Controls the default behavior of the webhook
     *
     * If it's enabled, then all objects will be mutated except the ones
     * that contain the value of matchingAnnotation as an annotation with a value of 'disabled'
     *
     * If it's false, then only objects
     * that contain the value of matchingAnnotation as an annotation with a value of 'enabled'
     * will be mutated
     */
    MatchPolicy policy = MatchPolicy.enabled

    String matchingAnnotation = "k8s-info-inject"


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
