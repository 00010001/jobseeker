package com.krzysztof.jobseeker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.krzysztof.jobseeker.domain.Job;
import com.krzysztof.jobseeker.service.crawler.JobSeekerService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawler")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    private JobSeekerService jobSeekerService;

    @Autowired
    public TestController(JobSeekerService jobSeekerService) {
        this.jobSeekerService = jobSeekerService;
    }

    @GetMapping("/testcrawler")
    @Timed
    public List<Job> testCrawler(@RequestParam(required = false, defaultValue = "false")
        boolean eagerload) {
        log.debug("test crawler");
        return jobSeekerService.getJobsByLocationAndPosition("net", "katowice");
    }

    @GetMapping("/jobs/{position}/{location}")
    @Timed
    public List<Job> getJobsByPositionAndLocation(
        @PathVariable String position,
        @PathVariable String location) {

        log.debug("test crawler");
        return jobSeekerService.getJobsByLocationAndPosition(position, location);
    }
}
