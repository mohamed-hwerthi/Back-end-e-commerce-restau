package com.foodsquad.FoodSquad.config.db;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This class implements Hibernate's CurrentTenantIdentifierResolver interface.
 * It is responsible for resolving the current tenant identifier (schema name)
 * for Hibernate's multi-tenancy functionality.
 * <p>
 * The tenant identifier is retrieved from the {@link TenantContext}, which typically stores
 * tenant information in a thread-local context.
 * <p>
 * If no tenant identifier is found, the resolver returns a default tenant schema name.
 * This ensures the application falls back to a known schema (usually 'public').
 * <p>
 * Hibernate uses this resolver to determine which tenant schema to use when opening sessions.
 */
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {

    private static final Logger logger = LoggerFactory.getLogger(CurrentTenantIdentifierResolverImpl.class);

    /**
     * Resolves the current tenant identifier by retrieving it from the TenantContext.
     * <p>
     * Called by Hibernate to determine which tenant schema to use for the current session.
     *
     * @return A string representing the current tenant schema identifier.
     *         Returns a default tenant identifier if none is set.
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            logger.debug("Resolved current tenant identifier: {}", tenantId);
        } else {
            logger.debug("No tenant identifier found, using default schema 'public'");
        }
        return (tenantId != null) ? tenantId : TenantContext.DEFAULT_TENANT;
    }

    /**
     * Indicates whether Hibernate should validate the tenant identifier of existing sessions.
     * <p>
     * Returning true ensures Hibernate checks tenant consistency across persistent sessions.
     *
     * @return true, to enable validation of existing current sessions' tenant identifiers.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        logger.debug("validateExistingCurrentSessions called");
        return true;
    }
}
