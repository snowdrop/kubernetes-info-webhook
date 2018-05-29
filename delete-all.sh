#!/usr/bin/env bash

oc delete MutatingWebhookConfiguration kubernetes-info-webhook
oc delete DeploymentConfig kubernetes-info-webhook
oc delete Service kubernetes-info-webhook
oc delete ConfigMap k8s-info-mutating-script
oc delete ConfigMap k8s-info-configuration
oc delete Secret kubernetes-info-webhook
oc delete ServiceAccount kubernetes-info-webhook
oc delete ClusterRole secret-reader
oc delete all --all