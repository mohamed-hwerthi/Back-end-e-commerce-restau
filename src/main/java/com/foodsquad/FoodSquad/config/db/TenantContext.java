package com.foodsquad.FoodSquad.config.db;

import java.util.Objects;

public class TenantContext {

    public static final String DEFAULT_TENANT = "public";

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static String getCurrentTenant() {

        String tenant = currentTenant.get();
        return Objects.requireNonNullElse(tenant, DEFAULT_TENANT);
    }

    public static void setCurrentTenant(String tenant) {

        currentTenant.set(tenant);
    }

    public static void clear() {

        currentTenant.remove();
    }


}
