package com.foodsquad.FoodSquad.config.db;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);

    private final transient DataSource dataSource;

    public MultiTenantConnectionProviderImpl(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    protected DataSource selectAnyDataSource() {

        return dataSource;
    }


    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {

        return dataSource;
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {

        String tenantId = tenantIdentifier != null ? tenantIdentifier.toString() : TenantContext.DEFAULT_TENANT;
        logger.info("Acquiring connection for tenant {}", tenantId);
        Connection connection;
        try {
            connection = getAnyConnection();
            try (Statement statement = connection.createStatement()) {
                statement.execute(String.format("SET search_path TO %s;", tenantId));
                try (ResultSet rs = statement.executeQuery("SHOW search_path")) {
                    if (rs.next()) logger.info("search_path is now: {}", rs.getString(1));
                }}
        } catch (SQLException e) {
            logger.error("Failed to acquire connection or set schema for tenant {}: {}", tenantId, e.getMessage());
            throw e;
        }
        return connection;
    }


    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {

        try (Statement statement = connection.createStatement()) {
            statement.execute("SET search_path TO public;");
        }
        releaseAnyConnection(connection);
    }

}



