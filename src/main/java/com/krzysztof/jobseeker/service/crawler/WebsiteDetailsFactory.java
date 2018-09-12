package com.krzysztof.jobseeker.service.crawler;

import com.krzysztof.jobseeker.domain.crawler.WebsiteDetails;
import com.krzysztof.jobseeker.domain.crawler.website.Praca;
import com.krzysztof.jobseeker.domain.crawler.website.Pracuj;
import com.krzysztof.jobseeker.domain.crawler.website.WebsiteName;
import org.springframework.stereotype.Service;

@Service
class WebsiteDetailsFactory {

    WebsiteDetails getWebsiteDetails(WebsiteName website) {
        if (website == null) {
            return null;
        }
        if (website.equals(WebsiteName.PRACUJ)) {
            return new Pracuj();
        }
        if (website.equals(WebsiteName.PRACA)) {
            return new Praca();
        }
        return null;
    }
}
