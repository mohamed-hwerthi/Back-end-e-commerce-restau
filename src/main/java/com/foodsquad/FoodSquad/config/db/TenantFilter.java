    package com.foodsquad.FoodSquad.config.db;

import com.foodsquad.FoodSquad.config.EncryptionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * A servlet filter responsible for extracting the tenant identifier (store ID) from the request header,
 * decrypting it, and setting it into the {@link TenantContext}.
 *
 * <p>This enables multi-tenancy by dynamically switching tenants based on the
 * {@code X-Store-ID} header sent with the request.</p>
 *
 * <p>The tenant context is cleared after processing the request to avoid leaking tenant information
 * across threads or requests.</p>
 *
 * <h3>Usage:</h3>
 * Clients must include the {@code X-Store-ID} header (encrypted store ID) in requests:
 * <pre>{@code
 * X-Store-ID: <encrypted-store-id>
 * }</pre>
 *
 * <p>If no header is present, the default tenant will be used.</p>
 */
@Slf4j
public class TenantFilter implements Filter {

    /**
     * The header key used to pass the encrypted tenant/store identifier.
     */
    public static final String TENANT_HEADER = "X-Store-ID";

    /**
     * Filters incoming requests to resolve tenant information.
     *
     * <p>Steps:</p>
     * <ol>
     *   <li>Extracts the {@code X-Store-ID} header from the request.</li>
     *   <li>Decrypts the header value using {@link EncryptionUtil}.</li>
     *   <li>Sets the decrypted tenant identifier in {@link TenantContext}.</li>
     *   <li>Proceeds with the request filter chain.</li>
     *   <li>Clears the tenant context after the request completes.</li>
     * </ol>
     *
     * @param request  the incoming HTTP servlet request
     * @param response the HTTP servlet response
     * @param chain    the filter chain to continue processing
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if the filter chain cannot be executed
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String encryptedStoreId = httpRequest.getHeader(TENANT_HEADER);

            if (encryptedStoreId == null || encryptedStoreId.isBlank()) {
                log.debug("No '{}' header found. Using default tenant context.", TENANT_HEADER);
            } else {
                setTenantContextFromHeader(encryptedStoreId);
            }
            chain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

    /** 
     * Attempts to decrypt the store ID from the request header and set the tenant context.
     *
     * @param encryptedStoreId the encrypted store ID from the request header
     */
    private void setTenantContextFromHeader(String encryptedStoreId) {
        try {
            String storeId = EncryptionUtil.decrypt(encryptedStoreId);
            storeId = storeId.replace("-", "_");
            String tenantIdentifier = "tenant_" + storeId;
            TenantContext.setCurrentTenant(tenantIdentifier);
            log.debug("Tenant successfully resolved and set to: {}", tenantIdentifier);
        } catch (Exception e) {
            log.error("Failed to decrypt or resolve tenant from store ID: {}", encryptedStoreId, e);
            throw new RuntimeException("Failed to decrypt or resolve tenant from store ID: " + encryptedStoreId, e);
        }
    }
}
