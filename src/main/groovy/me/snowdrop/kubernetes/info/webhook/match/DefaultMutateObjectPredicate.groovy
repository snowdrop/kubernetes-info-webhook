package me.snowdrop.kubernetes.info.webhook.match

import org.springframework.stereotype.Component

@Component
class DefaultMutateObjectPredicate implements MutateObjectPredicate {

    @Override
    boolean test(Input input) {
        if(input.matchPolicy == MatchPolicy.enabled) {

            if (!input.objectAnnotations) {//match since policy is enabled and there are no annotations
                return true
            }

            return !"disabled".equalsIgnoreCase(
                    input.objectAnnotations.get(input.matchingAnnotation))
        }
        else {
            return "enabled".equalsIgnoreCase(
                    input.objectAnnotations?.get(input.matchingAnnotation))
        }
    }
}
