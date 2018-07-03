package me.snowdrop.kubernetes.info.webhook


import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

import static me.snowdrop.kubernetes.info.webhook.TestData.exampleAdmissionReviewMap
import static org.assertj.core.api.Assertions.assertThat

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["k8s.info.enabled=true", "k8s.info.script.load-from-config-map=false"]
)
class LocalScriptIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate

    @Test
    void testLocalScriptThatDoesNotChangeTheObject() {
        final request = new HttpEntity<>(exampleAdmissionReviewMap())
        final response = restTemplate.exchange("/mutate", HttpMethod.POST, request, Map.class)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).containsKey("response")
        assertThat(response.body.response as Map).containsKey("patch")
        assertThat(response.body.response.patch as byte[])
                .isEqualTo(Base64.encoder.encode("[]" as byte[]))
    }
}
