package me.snowdrop.kubernetes.info.webhook

import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit4.SpringRunner

import static me.snowdrop.kubernetes.info.webhook.TestData.exampleAdmissionReviewBytes
import static org.assertj.core.api.Assertions.assertThat

@RunWith(SpringRunner.class)
@JsonTest
class AdmissionReviewJsonTest {

    @Autowired
    private JacksonTester<AdmissionReview> jacksonTester

    @Test
    void testDeserialize() {
        final admissionReview = jacksonTester.parseObject(exampleAdmissionReviewBytes())
        assertThat(admissionReview).isNotNull()
        assertThat(admissionReview.request).isNotNull()
        assertThat(admissionReview.request.namespace).isEqualTo("dummy")
        assertThat(admissionReview.request.userInfo).isNotNull()
        assertThat(admissionReview.request.userInfo.username).isEqualTo(
                "system:serviceaccount:kube-system:replicaset-controller"
        )
    }
}
