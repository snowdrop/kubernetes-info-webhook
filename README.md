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

## Preparation

```bash
oc new-project k8s-info
./create-signed-cert.sh
./create-default-configmaps.sh
./create-service-account.sh
```

## Deployment 

```bash
./mvnw clean compile fabric8:deploy -Popenshift
./create-webhook-configuration.sh
```