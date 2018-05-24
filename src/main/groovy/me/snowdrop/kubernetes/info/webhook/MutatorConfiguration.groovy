package me.snowdrop.kubernetes.info.webhook

import com.fasterxml.jackson.databind.ObjectMapper
import io.fabric8.kubernetes.client.KubernetesClient
import me.snowdrop.kubernetes.info.webhook.script.ScriptRunner
import me.snowdrop.kubernetes.info.webhook.script.SimpleGroovyScriptRunner
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

import java.nio.charset.StandardCharsets

@Configuration
class MutatorConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "k8s.info.enabled", havingValue = "false", matchIfMissing = false)
    static class DisabledConfiguration {

        @Bean
        Mutator noOpMutator() {
            return new Mutator.NoOpMutator()
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "k8s.info.enabled", havingValue = "true", matchIfMissing = true)
    static class EnabledConfiguration {

        @Bean(name = "scriptInputStream")
        @ConditionalOnProperty(
                name = "k8s.info.script.load-from-config-map",
                havingValue = "false", matchIfMissing = false
        )
        InputStream classPathScriptInputStream(KubernetesInfoProperties properties) {
            return new ClassPathResource(properties.script.name).getInputStream()
        }

        @Bean(name = "scriptInputStream")
        @ConditionalOnProperty(
                name = "k8s.info.script.load-from-config-map",
                havingValue = "true", matchIfMissing = true
        )
        InputStream configMapScriptInputStream(KubernetesInfoProperties properties,
                                               KubernetesClient kubernetesClient) {
            final configMap =
                    kubernetesClient.configMaps().withName(properties.script.configMapName).get()

            final groovyScript = configMap.getData().get(properties.script.name)

            return new ByteArrayInputStream(groovyScript.getBytes(StandardCharsets.UTF_8))
        }

        @Bean
        ScriptRunner scriptRunner(@Qualifier("scriptInputStream") InputStream scriptInputStream) {
            return new SimpleGroovyScriptRunner(scriptInputStream)
        }

        @Bean
        Mutator scriptRunnerMutator(KubernetesClient kubernetesClient, ScriptRunner scriptRunner,
                                    ObjectMapper objectMapper) {
            return new ScriptRunnerMutator(objectMapper, kubernetesClient, scriptRunner)
        }
    }


}
