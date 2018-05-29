#!/usr/bin/env bash

set -e

if [ ! -x "$(command -v kubectl)" ]; then
    echo "kubectl not found"
    exit 1
fi

if ! kubectl version > /dev/null 2>&1; then
    echo "Kubernetes server not accesible"
    exit 1
fi

cmd=kubectl
if kubectl api-versions | grep -q openshift
then
   cmd=oc
fi