package com.krzysztof.jobseeker.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SearchQuery.
 */
@Entity
@Table(name = "search_query")
public class SearchQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "position")
    private String position;

    @Column(name = "location")
    private String location;

    @ManyToMany
    @JoinTable(name = "search_query_job",
               joinColumns = @JoinColumn(name = "search_queries_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "jobs_id", referencedColumnName = "id"))
    private Set<Job> jobs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public SearchQuery position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public SearchQuery location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public SearchQuery jobs(Set<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public SearchQuery addJob(Job job) {
        this.jobs.add(job);
        job.getSearchQueries().add(this);
        return this;
    }

    public SearchQuery removeJob(Job job) {
        this.jobs.remove(job);
        job.getSearchQueries().remove(this);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchQuery searchQuery = (SearchQuery) o;
        if (searchQuery.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), searchQuery.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
            "id=" + getId() +
            ", position='" + getPosition() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
