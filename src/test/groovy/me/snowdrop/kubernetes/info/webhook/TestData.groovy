package me.snowdrop.kubernetes.info.webhook

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource

import static java.nio.file.Files.readAllBytes

final class TestData {

    private static final ObjectMapper objectMapper = new ObjectMapper()

    private TestData() {}

    static byte[] exampleAdmissionReviewBytes() {
        return readAllBytes(new ClassPathResource("admission-review.json").getFile().toPath())
    }

    static Map<String, Object> exampleAdmissionReviewMap() {
        return objectMapper.readValue(exampleAdmissionReviewBytes(), Map.class)
    }
}
