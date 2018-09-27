package com.krzysztof.jobseeker.service.crawler;

import com.krzysztof.jobseeker.domain.Job;
import com.krzysztof.jobseeker.domain.SearchQuery;
import com.krzysztof.jobseeker.domain.crawler.WebsiteDetails;
import com.krzysztof.jobseeker.domain.crawler.website.WebsiteName;
import com.krzysztof.jobseeker.repository.JobRepository;
import com.krzysztof.jobseeker.repository.SearchQueryRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {

    private final Logger logger = LogManager.getLogger(CrawlerService.class);
    private static final int DESCRIPTION_MAX_LENGHT = 252;
    private WebsiteDetailsFactory websiteDetailsFactory;
    private JobRepository jobRepository;
    private final SearchQueryRepository searchQueryRepository;

    @Autowired
    public CrawlerService(
        WebsiteDetailsFactory websiteDetailsFactory,
        JobRepository jobRepository,
        SearchQueryRepository searchQueryRepository) {
        this.websiteDetailsFactory = websiteDetailsFactory;
        this.jobRepository = jobRepository;
        this.searchQueryRepository = searchQueryRepository;
    }

    void crawlAllWebsites(SearchQuery searchQuery) {
        crawl(searchQuery, WebsiteName.PRACA);
        crawl(searchQuery, WebsiteName.PRACUJ);
        searchQueryRepository.save(searchQuery);
    }

    @Async
    private void crawl(SearchQuery searchQuery, WebsiteName website) {
        List<Job> jobList = parseJobs(searchQuery, website);
        assignSearchQueryToJobs(searchQuery, jobList);
        assignJobsToSearchQuery(searchQuery, jobList);
        jobRepository.saveAll(jobList);
    }

    private void assignJobsToSearchQuery(SearchQuery searchQuery, List<Job> jobList) {
        searchQuery.getJobs().addAll(jobList);
    }

    private void assignSearchQueryToJobs(SearchQuery searchQuery, List<Job> jobList) {
        for (Job job : jobList) {
            job.getSearchQueries().add(searchQuery);
        }
    }

    private List<Job> parseJobs(SearchQuery searchQuery, WebsiteName website) {
        WebsiteDetails websiteDetails = websiteDetailsFactory.getWebsiteDetails(website);
        String position = searchQuery.getPosition();
        String location = searchQuery.getLocation();
        String crawlUrl = websiteDetails.getCrawlUrl(position, location);
        Document document = getDocument(crawlUrl);

        List<Job> jobList = getJobsFromDocument(document, websiteDetails, searchQuery);
        if (jobList.isEmpty()) {
            logger.warn("no results found on " + crawlUrl);
        }
        return jobList;
    }

    private List<Job> getJobsFromDocument(Document document, WebsiteDetails websiteDetails,
        SearchQuery searchQuery) {

        Elements jobPostings = getJobPostings(document, websiteDetails.getJobPostingCssQuery());
        List<Job> jobList = new ArrayList<>();
        for (Element jobPosting : jobPostings) {
            Job job = createJobObject(jobPosting, websiteDetails, searchQuery);
            jobList.add(job);
        }
        return jobList;
    }

    private Job createJobObject(Element jobPosting, WebsiteDetails websiteDetails,
        SearchQuery searchQuery){

        Job job = new Job();
        job.setTitle(websiteDetails.getJobTitle(jobPosting));
        job.setUrl(websiteDetails.getJobUrl(jobPosting));
        job.setCompany(websiteDetails.getJobCompany(jobPosting));
        String jobDescription = websiteDetails.getJobDescription(jobPosting);
        if (jobDescription.length() > DESCRIPTION_MAX_LENGHT) {
            jobDescription = jobDescription.substring(0, DESCRIPTION_MAX_LENGHT) + "...";
        }
        job.setDescription(jobDescription);
        job.setWebsiteLogoUrl(websiteDetails.getWebsiteLogoUrl());
        job.setSearchQueries(Stream.of(searchQuery).collect(Collectors.toSet()));
        return job;
    }

    private Document getDocument(String url) {
        Document document = null;
        try {
            logger.debug("parsing from: " + url);
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.error("error while parsing from: " + url);
        }
        return document;
    }

    private Elements getJobPostings(Document document, String jobPostingCssQuery) {
        return document.select(jobPostingCssQuery);
    }
}
