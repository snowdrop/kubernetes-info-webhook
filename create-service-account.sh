#!/usr/bin/env bash

cat <<EOF | oc apply -f -
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: secret-reader
rules:
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["get", "watch", "list"]
EOF

cat <<EOF | oc apply -f -
apiVersion: v1
kind: ServiceAccount
metadata:
  name: kubernetes-info-webhook
EOF

for role in "view" "secret-reader"; do
  oc adm policy add-role-to-user ${role} system:serviceaccount:$(oc project -q):kubernetes-info-webhook -n $(oc project -q)
done