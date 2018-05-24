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
oc adm policy add-role-to-user view system:serviceaccount:$(oc project -q):kubernetes-info-webhook -n $(oc project -q)
```

## Deployment 

```bash
mvn clean compile fabric8:deploy -Popenshift
./create-webhook-configuration.sh
```