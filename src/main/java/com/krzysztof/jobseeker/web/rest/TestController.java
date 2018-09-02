package com.krzysztof.jobseeker.web.rest;

import com.krzysztof.jobseeker.domain.Job;
import com.krzysztof.jobseeker.domain.SearchQuery;
import com.krzysztof.jobseeker.repository.JobRepository;
import com.krzysztof.jobseeker.repository.SearchQueryRepository;
import com.krzysztof.jobseeker.service.crawler.JobSeekerService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    private JobRepository jobRepository;
    private SearchQueryRepository searchQueryRepository;
    private JobSeekerService jobSeekerService;

    @Autowired
    public TestController(JobRepository jobRepository,
        SearchQueryRepository searchQueryRepository,
        JobSeekerService jobSeekerService) {
        this.jobRepository = jobRepository;
        this.searchQueryRepository = searchQueryRepository;
        this.jobSeekerService = jobSeekerService;
    }

    @GetMapping("/api/crawler/")
    public List<Job> costam() {
        return jobSeekerService.getJobsByLocationAndPosition("java", "katowice");
    }

    @GetMapping("/api/mojtest")
    public List<Job> findBySearchQueries() {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setPosition("kierowca");
        searchQuery.setLocation("kato");
        searchQueryRepository.save(searchQuery);

        Job job = new Job();
        job.setTitle("kierowca tira");
        job.setUrl("http://kierowcy.pl");
        Set<Job> jobSet = new HashSet<>();
        jobSet.add(job);

        searchQuery.setJobs(jobSet);

        jobRepository.save(job);
        searchQueryRepository.save(searchQuery);

        return jobRepository.findBySearchQueries(searchQuery);
    }
}
