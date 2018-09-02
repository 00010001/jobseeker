package com.krzysztof.jobseeker.service.crawler;

import com.krzysztof.jobseeker.domain.Job;
import com.krzysztof.jobseeker.domain.SearchQuery;
import com.krzysztof.jobseeker.repository.JobRepository;
import com.krzysztof.jobseeker.repository.SearchQueryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerService {

    private final SearchQueryRepository searchQueryRepository;
    private final JobRepository jobRepository;
    private final CrawlerService crawlerService;

    public JobSeekerService(SearchQueryRepository searchQueryRepository,
        JobRepository jobRepository,
        CrawlerService crawlerService) {
        this.searchQueryRepository = searchQueryRepository;
        this.jobRepository = jobRepository;
        this.crawlerService = crawlerService;
    }

    public List<Job> getJobsByLocationAndPosition(String position, String location) {
        Optional<SearchQuery> optionalSearchQuery = searchQueryRepository.findByPositionAndLocation(position,
            location);
        if(optionalSearchQuery.isPresent()){
            return jobRepository.findBySearchQueries(optionalSearchQuery.get());
        } else {
            SearchQuery searchQuery = createAndSaveSearchQuery(position, location);
            crawlerService.crawlAllWebsites(searchQuery);
            return jobRepository.findBySearchQueries(searchQuery);
        }
    }

    private SearchQuery createAndSaveSearchQuery(String position, String location){
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setPosition(position);
        searchQuery.setLocation(location);
        searchQueryRepository.save(searchQuery);
        return searchQuery;
    }


}
