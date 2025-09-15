package com.foodsquad.FoodSquad.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
    private final TenantFilter tenantFilter;

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration() {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(tenantFilter);
        registration.addUrlPatterns("/api/*");
        registration.setName("tenantFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}