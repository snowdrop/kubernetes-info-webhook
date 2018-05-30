package me.snowdrop.kubernetes.info.webhook


import groovy.util.logging.Slf4j
import me.snowdrop.kubernetes.info.webhook.model.AdmissionReview
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
class MutatingController {

    private final MutatingService mutatingService

    MutatingController(MutatingService mutatingService) {
        this.mutatingService = mutatingService
    }

    @PostMapping("/mutate")
    AdmissionReview mutate(@RequestBody final AdmissionReview input) {
        log.debug("Received input:\n{}", input)

        final AdmissionReview output = mutatingService.mutate(input)

        log.debug("Adding the following response to AdmissionReview:\n{}", input.response)

        return output
    }

}
