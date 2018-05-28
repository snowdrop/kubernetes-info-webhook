#!/usr/bin/env bash

CERT_DIR=/etc/certs/tomcat
mkdir -p ${CERT_DIR}

oc get secret kubernetes-info-webhook-certs -o jsonpath='{.data.cert}' | base64 --decode > ${CERT_DIR}/server-cert.pem
oc get secret kubernetes-info-webhook-certs -o jsonpath='{.data.key}' | base64 --decode > ${CERT_DIR}/server-key.pem


openssl pkcs12 -export -in ${CERT_DIR}/server-cert.pem -inkey ${CERT_DIR}/server-key.pem -out ${CERT_DIR}/server-ks.pks -passout pass:tomcat