package me.snowdrop.kubernetes.info.webhook.match

import java.util.function.Predicate

interface MutateObjectPredicate extends Predicate<Input> {

    class Input {
        final MatchPolicy matchPolicy
        final String matchingAnnotation
        final Map<String, String> objectAnnotations

        Input(MatchPolicy matchPolicy, String matchingAnnotation,
              Map<String, String> objectAnnotations) {
            this.matchPolicy = matchPolicy
            this.matchingAnnotation = matchingAnnotation
            this.objectAnnotations = objectAnnotations
        }
    }

}