package com.krzysztof.jobseeker.repository;

import com.krzysztof.jobseeker.domain.Job;
import com.krzysztof.jobseeker.domain.SearchQuery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findBySearchQueries(SearchQuery searchQuery);

}
