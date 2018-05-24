package me.snowdrop.kubernetes.info.webhook

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(KubernetesInfoProperties)
class KubernetesInfoWebhookApplication {

    static void main(String[] args) {
        SpringApplication.run(KubernetesInfoWebhookApplication.class, args)
    }
}
