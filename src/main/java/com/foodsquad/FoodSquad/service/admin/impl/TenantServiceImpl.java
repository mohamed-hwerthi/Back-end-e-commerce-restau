package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.admin.dec.TenantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
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
    @Transactional
    public void createTenant(String storeId, User user) {
        String safeSchemaName = "tenant_" + storeId.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String tempSchemaName = safeSchemaName + "_tmp";

        if (schemaExists(safeSchemaName) || schemaExists(tempSchemaName)) {
            throw new IllegalStateException("Tenant schema already exists: " + safeSchemaName);
        }

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE SCHEMA " + tempSchemaName);
            Flyway flyway = Flyway.configure()
                    .schemas(tempSchemaName)
                    .defaultSchema(tempSchemaName)
                    .locations("classpath:db/migration/migration_tenant")
                    .dataSource(dataSource)
                    .load();

            flyway.migrate();
            stmt.execute("ALTER SCHEMA " + tempSchemaName + " RENAME TO " + safeSchemaName);

            log.info("✅ Tenant schema created and migrated successfully: {}", safeSchemaName);

        } catch (Exception ex) {
            log.error("❌ Failed to create tenant schema {}: {}", safeSchemaName, ex.getMessage());
            try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
                if (schemaExists(tempSchemaName)) {
                    stmt.execute("DROP SCHEMA " + tempSchemaName + " CASCADE");
                }
            } catch (Exception cleanupEx) {
                log.error("⚠️ Failed to clean up temp schema {}: {}", tempSchemaName, cleanupEx.getMessage());
            }
            throw new IllegalStateException("Failed to create tenant schema: " + safeSchemaName, ex);
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
