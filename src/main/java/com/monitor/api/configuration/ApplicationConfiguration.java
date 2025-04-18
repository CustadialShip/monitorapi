package com.monitor.api.configuration;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * Configuration class for application-level settings.
 * <p>
 * This configuration defines a filter that adds support for ETag HTTP headers,
 * improving performance by allowing conditional requests and reducing bandwidth.
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * Registers a {@link ShallowEtagHeaderFilter} as a bean.
     * <p>
     * This filter generates ETag headers based on the response content.
     * It enables the use of weak ETags, which are suitable for most HTTP caching scenarios.
     *
     * @return the configured {@link Filter} bean for ETag support
     */
    @Bean
    public Filter shallowEtagHeaderFilter() {
        ShallowEtagHeaderFilter shallowEtagHeaderFilter = new ShallowEtagHeaderFilter();
        shallowEtagHeaderFilter.setWriteWeakETag(true);
        return shallowEtagHeaderFilter;
    }
}
