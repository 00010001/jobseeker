package com.krzysztof.jobseeker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Job.
 */
@Entity
@Table(name = "job")
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "jobs")
    @JsonIgnore
    private Set<SearchQuery> searchQueries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Job url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public Job title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<SearchQuery> getSearchQueries() {
        return searchQueries;
    }

    public Job searchQueries(Set<SearchQuery> searchQueries) {
        this.searchQueries = searchQueries;
        return this;
    }

    public Job addSearchQuery(SearchQuery searchQuery) {
        this.searchQueries.add(searchQuery);
        searchQuery.getJobs().add(this);
        return this;
    }

    public Job removeSearchQuery(SearchQuery searchQuery) {
        this.searchQueries.remove(searchQuery);
        searchQuery.getJobs().remove(this);
        return this;
    }

    public void setSearchQueries(Set<SearchQuery> searchQueries) {
        this.searchQueries = searchQueries;
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
        Job job = (Job) o;
        if (job.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), job.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Job{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
