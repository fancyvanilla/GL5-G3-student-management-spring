package org.microservices.enrollment.client;

import org.microservices.enrollment.StudentSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service")
public interface StudentClient {
    @GetMapping("/students/{id}/summary")
    StudentSummaryDto getStudentSummary(@PathVariable("id") Long id);
}

