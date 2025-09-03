package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.service.declaration.TenantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @PersistenceContext
    private EntityManager entityManager;

    private final DataSource dataSource;


    @Autowired
    public TenantServiceImpl(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public void createTenant(String storeId ) {

        String safeSchemaName = "tenant" +"_" + storeId.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        if (!schemaExists(safeSchemaName)) {
            Flyway flyway = Flyway.configure()
                    .schemas(safeSchemaName)
                    .locations("classpath:db/migration/migration_tenant")
                    .dataSource(dataSource)
                    .load();
            flyway.migrate();
        }
    }

    private boolean schemaExists(String schemaName) {

        String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = :schemaName";
        List<?> result = entityManager.createNativeQuery(sql)
                .setParameter("schemaName", schemaName)
                .getResultList();
        return !result.isEmpty();
    }

}
