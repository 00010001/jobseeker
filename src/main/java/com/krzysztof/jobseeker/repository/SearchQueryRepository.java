package com.krzysztof.jobseeker.repository;

import com.krzysztof.jobseeker.domain.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SearchQuery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {

    @Query(value = "select distinct search_query from SearchQuery search_query left join fetch search_query.jobs",
        countQuery = "select count(distinct search_query) from SearchQuery search_query")
    Page<SearchQuery> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct search_query from SearchQuery search_query left join fetch search_query.jobs")
    List<SearchQuery> findAllWithEagerRelationships();

    @Query("select search_query from SearchQuery search_query left join fetch search_query.jobs where search_query.id =:id")
    Optional<SearchQuery> findOneWithEagerRelationships(@Param("id") Long id);

}
