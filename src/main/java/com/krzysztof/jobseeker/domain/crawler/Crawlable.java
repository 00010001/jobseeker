package com.krzysztof.jobseeker.domain.crawler;

import org.jsoup.nodes.Element;

public interface Crawlable {
    String getCrawlUrl(String position, String location);
    String getJobTitle(Element element);
    String getJobUrl(Element element);
}
