package com.krzysztof.jobseeker.service.crawler;

import com.krzysztof.jobseeker.domain.Job;
import com.krzysztof.jobseeker.domain.SearchQuery;
import com.krzysztof.jobseeker.domain.crawler.WebsiteDetails;
import com.krzysztof.jobseeker.domain.crawler.website.WebsiteName;
import com.krzysztof.jobseeker.repository.JobRepository;
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

    private WebsiteDetailsFactory websiteDetailsFactory;
    private JobRepository jobRepository;

    @Autowired
    public CrawlerService(
        WebsiteDetailsFactory websiteDetailsFactory,
        JobRepository jobRepository) {
        this.websiteDetailsFactory = websiteDetailsFactory;
        this.jobRepository = jobRepository;
    }

    void crawlAllWebsites(SearchQuery searchQuery) {
        crawl(searchQuery, WebsiteName.PRACA);
        crawl(searchQuery, WebsiteName.PRACUJ);
    }

    @Async
    private void crawl(SearchQuery searchQuery, WebsiteName websiteName) {
        List<Job> jobList = parseJobs(searchQuery, websiteName);
        jobRepository.saveAll(jobList);
    }

    private List<Job> parseJobs(SearchQuery searchQuery, WebsiteName websiteName) {
        WebsiteDetails websiteDetails = websiteDetailsFactory.getWebsiteDetails(websiteName);
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
            String jobTitle = websiteDetails.getJobTitle(jobPosting);
            String jobUrl = websiteDetails.getJobUrl(jobPosting);
            Job job = new Job();
            job.setTitle(jobTitle);
            job.setUrl(jobUrl);
            job.setSearchQueries(Stream.of(searchQuery).collect(Collectors.toSet()));
            jobList.add(job);
        }
        return jobList;
    }

    private Document getDocument(String url) {
        Document document = null;
        try {
            logger.info("parsing from: " + url);
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
