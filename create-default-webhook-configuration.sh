#!/usr/bin/env bash

source determine-cmd.sh

CA_BUNDLE=$(${cmd} get configmap -n kube-system extension-apiserver-authentication -o=jsonpath='{.data.client-ca-file}' | base64 | tr -d '\n')

cat <<EOF | ${cmd} apply -f -
apiVersion: admissionregistration.k8s.io/v1beta1
kind: MutatingWebhookConfiguration
metadata:
  name: kubernetes-info-webhook
webhooks:
  - name: kubernetes-info.snowdrop.me
    clientConfig:
      service:
        name: kubernetes-info-webhook
        namespace: k8s-info
        path: "/mutate"
      caBundle: ${CA_BUNDLE}
    rules:
      - operations: [ "CREATE" ]
        apiGroups: [""]
        apiVersions: ["v1"]
        resources: ["pods"]
    failurePolicy: Ignore
EOF
