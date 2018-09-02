package com.krzysztof.jobseeker.domain.crawler;

import org.jsoup.nodes.Element;

public class WebsiteDetails implements Crawlable{

    private String jobPostingCssQuery;
    private String searchUrl;

    public WebsiteDetails(String jobPostingCssQuery, String searchUrl) {
        this.jobPostingCssQuery = jobPostingCssQuery;
        this.searchUrl = searchUrl;
    }

    protected String getSearchUrl() {
        return searchUrl;
    }

    public String getJobPostingCssQuery() {
        return jobPostingCssQuery;
    }

    @Override
    public String getCrawlUrl(String position, String location) {
        return null;
    }

    @Override
    public String getJobTitle(Element element) {
        return null;
    }

    @Override
    public String getJobUrl(Element element) {
        return null;
    }

}
