package me.snowdrop.kubernetes.info.webhook

import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview

interface MutatingService {

    AdmissionReview mutate(AdmissionReview input)
}