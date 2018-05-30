# Purpose 

To make it easier to create / deploy Kubernetes Mutating WebHook applications

## What does the typical Mutating Webhook application look like

The purpose of the mutating webhook is to "mutate" Kubernetes objects.
Essentially such an application is a normal Kubernetes application (albeit with some requirements that will be mentioned later)
that is called by Kubernetes under certain conditions.

In order for an application to be a valid Mutating Webhook application the following prerequisites need to be met

* The application needs to be accessible via Kubernetes Service object on port 443
* The application needs to accept an `AdmissionReview` object and return a new `AdmissionReview` object
that contains an `AdmissionResponse` whose most important field is the `jsonpatch` that needs to be applied to the 
incoming object
* The certificate used by the application for the HTTPS communication (initiated by Kubernetes to the application) needs 
to be trusted by the cluster
* A `MutatingWebhookConfiguration` needs to be deployed to the cluster containing configuration of what kind of Requests
the mutating webhook will handle as well as the certificate `caBundle`
   

## Prerequisites

The cluster allows mutating webhooks

For minishift the following command can be run to enable such capability

```bash
 minishift openshift config set --target master --patch '{ "admissionConfig": { "pluginConfig": { "MutatingAdmissionWebhook": { "configuration": {  "apiVersion": "v1",  "disable": false,  "kind": "DefaultAdmissionConfig" } } } }, "kubernetesMasterConfig": { "controllerArguments": { "cluster-signing-cert-file": [ "ca.crt" ], "cluster-signing-key-file": [ "ca.key" ] } } }'
```

## Preparation (these steps need to be run by an administrator)

```bash
oc new-project k8s-info
./create-signed-cert.sh
./create-default-configmaps.sh
./create-service-account.sh
```

These steps will create the following:

* A ServiceAccount named `kubernetes-info-webhook` that contains the `view` and `secret-reader` roles in the `k8s-info` namespace
* A secret named `kubernetes-info-webhook` that contain the certificate and private key needed for HTTPS communication
between the application and the cluster.
This secret is read by an init container when the application runs in order to create the keystore that Tomcat needs to implement for HTTPS 
* A ConfigMap named `k8s-info-configuration` that contains the application configuration which is read when the application starts
* A ConfigMap named `k8s-info-mutating-script` that contains the actual script that will mutate the incoming object

## Configuration

Pay special attention to the values of `policy` and `matchingAnnotation` in [KubernetesInfoProperties](src/main/groovy/me/snowdrop/kubernetes/info/webhook/KubernetesInfoProperties.groovy)
The values you specify for these fields determine which objects will be mutated

Specifically, if `policy` is enabled, then all objects will be mutated except the ones
that contain the value of `matchingAnnotation` as an annotation with a value of `disabled`

If it's false, then only objects that contain the value of `matchingAnnotation` as an annotation with a value of `enabled`
will be mutated

## Deployment 

```bash
./mvnw clean compile fabric8:deploy -Popenshift
./create-default-webhook-configuration.sh
```

The `create-default-webhook-configuration.sh` will create a default `MutatingWebhookConfiguration`

## Teardown

./delete-all.sh 

## TODO 

* Provide configuration options for scripts 
* Provide deployment means other than FMP
