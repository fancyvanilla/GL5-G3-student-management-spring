package org.microservices.enrollment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceStartupListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Enrollment Service is ready and listening on port 8082");
    }
}
