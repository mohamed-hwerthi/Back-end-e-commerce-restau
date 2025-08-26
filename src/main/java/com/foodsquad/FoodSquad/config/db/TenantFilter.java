package com.foodsquad.FoodSquad.config.db;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class TenantFilter implements Filter {

    public static final String TENANT_HEADER = "X-TenantID";



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String tenantId = httpRequest.getHeader(TENANT_HEADER);
            if (tenantId == null) {
                tenantId = TenantContext.DEFAULT_TENANT;
            }
            TenantContext.setCurrentTenant(tenantId);

            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

}
