#!/usr/bin/env bash

source determine-cmd.sh

cat <<EOF | ${cmd} apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: k8s-info-configuration
data:
  application.properties: |
    k8s.info.enabled=true
EOF


cat <<EOF | ${cmd} apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: k8s-info-mutating-script
data:
  mutator.groovy: |
    import com.fasterxml.jackson.core.type.TypeReference
    import com.fasterxml.jackson.databind.ObjectMapper
    import io.fabric8.kubernetes.api.model.EnvVar
    import io.fabric8.kubernetes.client.KubernetesClient
    import io.fabric8.openshift.client.OpenShiftClient
    import me.snowdrop.kubernetes.info.webhook.script.ScriptRunner

    //read the script's input
    final ScriptRunner.Input input = binding.getVariable("input")
    final Map<String, Object> originalObjectCopy = input.originalObjectCopy
    final KubernetesClient kubernetesClient = input.kubernetesClient
    final ObjectMapper objectMapper = input.objectMapper


    //add KUBERNETES_GIT_VERSION and KUBERNETES_DISTRO to each one of the containers of
    //the originalObjectCopy is assumed to be a pod
    originalObjectCopy.spec.containers.each { container ->
        final List<EnvVar> containerEnv = containerEnv(container, objectMapper)
        containerEnv.add(
            createNameValueEnvVar("KUBERNETES_GIT_VERSION", kubernetesClient.version.gitVersion)
        )
        containerEnv.add(
            createNameValueEnvVar(
                "KUBERNETES_DISTRO",
                kubernetesClient.isAdaptable(OpenShiftClient.class) ? "openshift" : "vanilla"
            )
        )
    }

    //return the updated object - pod in this case
    return originalObjectCopy


    private List<EnvVar> containerEnv(Map<String, Object> container, ObjectMapper objectMapper) {
        if (container.containsKey("env")) {
            return objectMapper.convertValue(container.env, new TypeReference<List<EnvVar>>(){})

        }

        return container.env = new ArrayList<>()
    }

    private EnvVar createNameValueEnvVar(String name, value) {
        final kubernetesVersionEnvVar = new EnvVar()

        kubernetesVersionEnvVar.name = name
        kubernetesVersionEnvVar.value = value

        return kubernetesVersionEnvVar
    }
EOF