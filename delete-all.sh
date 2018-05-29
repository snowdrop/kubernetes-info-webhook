#!/usr/bin/env bash

source determine-cmd.sh

${cmd} delete MutatingWebhookConfiguration kubernetes-info-webhook
${cmd} delete DeploymentConfig kubernetes-info-webhook
${cmd} delete Service kubernetes-info-webhook
${cmd} delete ConfigMap k8s-info-mutating-script
${cmd} delete ConfigMap k8s-info-configuration
${cmd} delete Secret kubernetes-info-webhook
${cmd} delete ServiceAccount kubernetes-info-webhook
${cmd} delete ClusterRole secret-reader
${cmd} delete all --all