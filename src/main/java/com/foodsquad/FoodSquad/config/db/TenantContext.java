package com.foodsquad.FoodSquad.config.db;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Utility class to manage the current tenant context using ThreadLocal.
 */
@Slf4j
public class TenantContext {

    public static final String DEFAULT_TENANT = "public";

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    /**
     * Get the current tenant from the ThreadLocal context.
     * If no tenant is set, returns the default tenant.
     *
     * @return the current tenant or default tenant
     */
    public static String getCurrentTenant() {
        String tenant = currentTenant.get();
        String resolvedTenant = Objects.requireNonNullElse(tenant, DEFAULT_TENANT);
        log.debug("Retrieved current tenant: {}", resolvedTenant);
        return resolvedTenant;
    }

    /**
     * Set the current tenant in the ThreadLocal context.
     *
     * @param tenant the tenant identifier to set
     */
    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
        log.info("Tenant set to: {}", tenant);
    }

    /**
     * Clear the current tenant from the ThreadLocal context.
     */
    public static void clear() {
        log.debug("Clearing current tenant: {}", currentTenant.get());
        currentTenant.remove();
    }
}
