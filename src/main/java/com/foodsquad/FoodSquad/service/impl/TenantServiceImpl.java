package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.TenantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {

    private final DataSource dataSource;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTenant(String storeId, User user) {
        String safeSchemaName = "tenant_" + storeId.toLowerCase().replaceAll("[^a-z0-9_]", "_");

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
