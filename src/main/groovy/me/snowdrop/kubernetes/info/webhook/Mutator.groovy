package me.snowdrop.kubernetes.info.webhook

import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview

interface Mutator {

    Response mutate(Map<String, Object> originalObjectCopy,
                               AdmissionReview admissionReview)

    class Response {
        boolean mutated
        Map<String, Object> newObject

        private Response(boolean mutated, Map<String, Object> newObject) {
            this.mutated = mutated
            this.newObject = newObject
        }

        static Response noMutation(Map<String, Object> originalObject) {
            return new Response(false, originalObject)
        }

        static Response mutated(Map<String, Object> newObject) {
            return new Response(true, newObject)
        }
    }

    class NoOpMutator implements Mutator {

        @Override
        Response mutate(Map<String, Object> originalObjectCopy,
                                   AdmissionReview admissionReview) {
            return Response.noMutation(originalObjectCopy)
        }
    }
}