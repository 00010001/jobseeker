package com.krzysztof.jobseeker.domain.crawler;

import org.jsoup.nodes.Element;

public class WebsiteDetails implements Crawlable{

    private String jobPostingCssQuery;
    private String searchUrl;
    private String websiteLogoUrl;

    public WebsiteDetails(String jobPostingCssQuery, String searchUrl, String logoUrl) {
        this.jobPostingCssQuery = jobPostingCssQuery;
        this.searchUrl = searchUrl;
        this.websiteLogoUrl = logoUrl;
    }

    protected String getSearchUrl() {
        return searchUrl;
    }

    public String getJobPostingCssQuery() {
        return jobPostingCssQuery;
    }

    public String getWebsiteLogoUrl() {
        return websiteLogoUrl;
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
    public String getJobDescription(Element element) {
        return null;
    }

    @Override
    public String getJobUrl(Element element) {
        return null;
    }

}
