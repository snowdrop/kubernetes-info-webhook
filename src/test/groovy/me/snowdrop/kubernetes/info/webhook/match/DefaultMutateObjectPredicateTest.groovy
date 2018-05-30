package me.snowdrop.kubernetes.info.webhook.match

import org.junit.Test

import static MutateObjectPredicate.Input
import static me.snowdrop.kubernetes.info.webhook.match.MatchPolicy.disabled
import static me.snowdrop.kubernetes.info.webhook.match.MatchPolicy.enabled
import static org.assertj.core.api.Assertions.assertThat

class DefaultMutateObjectPredicateTest {

    private static final String MATCHING_ANNOTATION = "whatever"
    private final sut = new DefaultMutateObjectPredicate()

    @Test
    void policyEnabledAndNullAnnotations() {
        assertThat(sut.test(new Input(enabled, MATCHING_ANNOTATION, null))).isTrue()
    }

    @Test
    void policyEnabledAndEmptyAnnotations() {
        assertThat(sut.test(new Input(enabled, MATCHING_ANNOTATION, [:]))).isTrue()
    }

    @Test
    void policyEnabledAndNoMatchingAnnotation() {
        assertThat(sut.test(new Input(enabled, MATCHING_ANNOTATION, [foo : 'bar']))).isTrue()
    }

    @Test
    void policyEnabledAndMatchingAnnotationEnabled() {
        assertThat(sut.test(new Input(
                enabled, MATCHING_ANNOTATION, [foo : 'bar', (MATCHING_ANNOTATION) : 'enabled']))
        ).isTrue()
    }

    @Test
    void policyEnabledAndMatchingAnnotationDisabled() {
        assertThat(sut.test(new Input(
                enabled, MATCHING_ANNOTATION, [foo : 'bar', (MATCHING_ANNOTATION) : 'disabled']))
        ).isFalse()
    }

    @Test
    void policyDisabledAndNullAnnotations() {
        assertThat(sut.test(new Input(disabled, MATCHING_ANNOTATION, null))).isFalse()
    }

    @Test
    void policyDisabledAndEmptyAnnotations() {
        assertThat(sut.test(new Input(disabled, MATCHING_ANNOTATION, [:]))).isFalse()
    }

    @Test
    void policyDisabledAndNoMatchingAnnotation() {
        assertThat(sut.test(new Input(disabled, MATCHING_ANNOTATION, [foo : 'bar']))).isFalse()
    }

    @Test
    void policyDisabledAndMatchingAnnotationEnabled() {
        assertThat(sut.test(new Input(
                disabled, MATCHING_ANNOTATION, [foo : 'bar', (MATCHING_ANNOTATION) : 'enabled']))
        ).isTrue()
    }

    @Test
    void policyDisabledAndMatchingAnnotationDisabled() {
        assertThat(sut.test(new Input(
                disabled, MATCHING_ANNOTATION, [foo : 'bar', (MATCHING_ANNOTATION) : 'disabled']))
        ).isFalse()
    }
}