package com.foodsquad.FoodSquad.config.db;

import com.foodsquad.FoodSquad.config.context.TenantContext;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A custom implementation of {@link AbstractDataSourceBasedMultiTenantConnectionProviderImpl}
 * for handling multi-tenancy using PostgreSQL schemas.
 *
 * <p>This class provides database connections for different tenants based on their tenant identifier.
 * It dynamically sets the PostgreSQL schema (search_path) for each connection, allowing schema-based
 * multi-tenancy. When no tenant identifier is provided, it defaults to the {@code public} schema.</p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Obtains a single shared {@link DataSource} for all tenants.</li>
 *   <li>Dynamically switches the schema using {@code SET search_path}.</li>
 *   <li>Ensures connections are reset to the {@code public} schema upon release.</li>
 * </ul>
 */
@Component
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);

    /**
     * Shared data source used for all tenants.
     */
    private final transient DataSource dataSource;

    /**
     * Constructs a {@link MultiTenantConnectionProviderImpl} with the given data source.
     *
     * @param dataSource the shared data source
     */
    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Selects a default data source to use when no tenant-specific information is available.
     *
     * @return the default {@link DataSource}
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    /**
     * Selects the data source for a specific tenant. In this implementation,
     * all tenants share the same data source.
     *
     * @param tenantIdentifier the tenant identifier (schema name)
     * @return the same {@link DataSource} for all tenants
     */
    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {
        return dataSource;
    }

    /**
     * Obtains a database connection for a given tenant and sets the schema using {@code search_path}.
     *
     * @param tenantIdentifier the tenant identifier (schema name)
     * @return a connection configured for the specified tenant
     * @throws SQLException if a database access error occurs or the schema cannot be set
     */
    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        String tenantId = tenantIdentifier != null
                ? tenantIdentifier.toString()
                : TenantContext.DEFAULT_TENANT;

            logger.info("Acquiring connection for tenant: {}", tenantId);

        Connection connection;
        try {
            connection = getAnyConnection();
            try (Statement statement = connection.createStatement()) {
                statement.execute(String.format("SET search_path TO %s;", tenantId));
                try (ResultSet rs = statement.executeQuery("SHOW search_path")) {
                    if (rs.next()) {
                        logger.info("search_path is now: {}", rs.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to acquire connection or set schema for tenant {}: {}", tenantId, e.getMessage());
            throw e;
        }
        return connection;
    }


    /**
     * Releases the given database connection and resets its schema to {@code public}.
     *
     * @param tenantIdentifier the tenant identifier (unused here)
     * @param connection       the connection to release
     * @throws SQLException if an error occurs while resetting the schema or releasing the connection
     */
    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO public;");
        }
        releaseAnyConnection(connection);
    }
}
