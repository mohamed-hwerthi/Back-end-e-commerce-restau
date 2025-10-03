package com.foodsquad.FoodSquad.config.web;

import com.foodsquad.FoodSquad.config.context.StoreSlugContext;
import com.foodsquad.FoodSquad.config.context.TenantContext;
import com.foodsquad.FoodSquad.config.db.StoreSlugResolver;
import com.foodsquad.FoodSquad.config.security.EncryptionUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * {@code TenantFilter} is a servlet filter responsible for dynamically resolving the tenant
 * (database schema) for each incoming request. It determines the tenant either by:
 * <ul>
 *   <li>Decrypting an encrypted tenant identifier from the request header (for admin panel usage)</li>
 *   <li>Extracting a store slug from the query parameter {@code storeSlug} (for storefront usage)</li>
 *   <li>Falling back to a default tenant if no tenant information is found</li>
 * </ul>
 */
@Slf4j
@Component
public class TenantFilter implements Filter {

    /**
     * Header name used to pass encrypted tenant (store) ID for admin panel APIs.
     */
    public static final String TENANT_HEADER = "X-Store-ID";

    /**
     * Query parameter name for the store slug.
     */
    public static final String STORE_SLUG_PARAM = "storeSlug";

    private final StoreSlugResolver storeSlugResolver;

    @Autowired
    public TenantFilter(StoreSlugResolver storeSlugResolver) {
        this.storeSlugResolver = storeSlugResolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            if (shouldSkipTenantFilter(httpRequest)) {
                setDefaultTenant();
            } else {
                resolveTenant(httpRequest);
            }

            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            StoreSlugContext.clear();
        }
    }

    /**
     * Decides how to resolve the tenant (header, slug, or fallback).
     */
    private void resolveTenant(HttpServletRequest httpRequest) {
        String encryptedStoreId = httpRequest.getHeader(TENANT_HEADER);

        if (StringUtils.hasText(encryptedStoreId)) {
            resolveTenantFromHeader(encryptedStoreId);
            return;
        }

        String slug = httpRequest.getParameter(STORE_SLUG_PARAM);
        if (StringUtils.hasText(slug)) {
            resolveTenantFromSlug(slug);
        } else {
            log.debug("No tenant info found; using default tenant.");
            setDefaultTenant();
        }
    }

    /**
     * Resolves tenant using encrypted store ID from header.
     */
    private void resolveTenantFromHeader(String encryptedStoreId) {
        try {
            String storeId = EncryptionUtil.decrypt(encryptedStoreId).replace("-", "_");
            String tenant = "tenant_" + storeId;
            TenantContext.setCurrentTenant(tenant);
            log.debug("Tenant resolved from header: {}", tenant);
        } catch (Exception e) {
            log.error("Failed to decrypt tenant from header", e);
            throw new RuntimeException("Tenant resolution failed", e);
        }
    }

    /**
     * Resolves tenant using a store slug.
     */
    private void resolveTenantFromSlug(String slug) {
        try {
            StoreSlugContext.setCurrentStoreSlug(slug);
            String tenant = storeSlugResolver.resolveTenantFromSlug(slug);
            if (!ObjectUtils.isEmpty(tenant)) {
                TenantContext.setCurrentTenant(tenant);
                log.debug("Tenant resolved from slug: {}", tenant);
            } else {
                log.warn("No tenant found for slug: {}", slug);
                setDefaultTenant();
            }
        } catch (Exception e) {
            log.error("Failed to resolve tenant from slug {}", slug, e);
            setDefaultTenant();
        }
    }

    /**
     * Whether the tenant filter should be skipped (e.g., store management APIs).
     */
    private boolean shouldSkipTenantFilter(HttpServletRequest httpRequest) {
        String path = httpRequest.getRequestURI();
        return StringUtils.hasText(path) && path.startsWith("/api/stores");
    }

    /**
     * Sets tenant to the default tenant.
     */
    private void setDefaultTenant() {
        TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
    }
}
