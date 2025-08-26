package com.foodsquad.FoodSquad.config.db;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {

    private static final Logger logger = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            logger.debug("Resolved current tenant identifier: {}", tenantId);
        } else {
            logger.debug("No tenant identifier found, using default schema 'public'");
        }
        return (tenantId != null) ? tenantId :TenantContext.DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        logger.debug("validateExistingCurrentSessions called");
        return true;
    }
}
