#!/usr/bin/env bash

cat MutatingWebhookConfiguration.yml | ./patch-ca-bundle.sh | oc apply -f -
