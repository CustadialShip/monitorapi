package com.monitor.api.configuration;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;


@Configuration
public class ApplicationConfiguration {

    @Bean
    public Filter shallowEtagHeaderFilter() {
        ShallowEtagHeaderFilter shallowEtagHeaderFilter = new ShallowEtagHeaderFilter();
        shallowEtagHeaderFilter.setWriteWeakETag(true);
        return shallowEtagHeaderFilter;
    }
}
