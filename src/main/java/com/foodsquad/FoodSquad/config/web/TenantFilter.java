package com.foodsquad.FoodSquad.config.web;

import com.foodsquad.FoodSquad.config.security.EncryptionUtil;
import com.foodsquad.FoodSquad.config.db.StoreSlugResolver;
import com.foodsquad.FoodSquad.config.context.TenantContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * TenantFilter is a servlet filter responsible for dynamically resolving the tenant (database schema)
 * for each incoming request. It determines the tenant either by:
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
            String path = httpRequest.getRequestURI();

            if (shouldSkipTenantFilter(path)) {
                TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
                chain.doFilter(request, response);
                return;
            }

            String encryptedStoreId = httpRequest.getHeader(TENANT_HEADER);
            if (StringUtils.hasText(encryptedStoreId)) {
                setTenantContextFromHeader(encryptedStoreId);
            } else {
                String slug = httpRequest.getParameter(STORE_SLUG_PARAM);
                if (StringUtils.hasText(slug)) {
                    setTenantContextFromSlug(slug);
                } else {
                    log.debug("No tenant info found; using default.");
                    TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
                }
            }

            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private void setTenantContextFromHeader(String encryptedStoreId) {
        try {
            String storeId = EncryptionUtil.decrypt(encryptedStoreId).replace("-", "_");
            TenantContext.setCurrentTenant("tenant_" + storeId);
            log.debug("Tenant resolved from header: tenant_{}", storeId);
        } catch (Exception e) {
            log.error("Failed to decrypt tenant from header", e);
            throw new RuntimeException("Tenant resolution failed", e);
        }
    }

    private void setTenantContextFromSlug(String slug) {
        try {
            String tenant = storeSlugResolver.resolveTenantFromSlug(slug);
            if (!ObjectUtils.isEmpty(tenant)) {
                TenantContext.setCurrentTenant(tenant);
                log.debug("Tenant resolved from slug: {}", tenant);
            } else {
                log.warn("No tenant found for slug: {}", slug);
                TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
            }
        } catch (Exception e) {
            log.error("Failed to resolve tenant from slug {}", slug, e);
            TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
        }
    }

    private boolean shouldSkipTenantFilter(String path) {
        return StringUtils.hasText(path) && path.startsWith("/api/stores");
    }
}
