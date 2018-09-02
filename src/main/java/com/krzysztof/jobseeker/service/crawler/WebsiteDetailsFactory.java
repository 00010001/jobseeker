package com.krzysztof.jobseeker.service.crawler;

import com.krzysztof.jobseeker.domain.crawler.WebsiteDetails;
import com.krzysztof.jobseeker.domain.crawler.website.Praca;
import com.krzysztof.jobseeker.domain.crawler.website.Pracuj;
import com.krzysztof.jobseeker.domain.crawler.website.WebsiteName;
import org.springframework.stereotype.Service;

@Service
class WebsiteDetailsFactory {

    WebsiteDetails getWebsiteDetails(WebsiteName websiteName) {
        if (websiteName == null) {
            return null;
        }
        if (websiteName.equals(WebsiteName.PRACUJ)) {
            return new Pracuj();
        }
        if (websiteName.equals(WebsiteName.PRACA)) {
            return new Praca();
        }
        return null;
    }
}
