package com.foodsquad.FoodSquad.config.db;

import jakarta.annotation.PostConstruct;
import org.hibernate.cfg.MultiTenancySettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up Hibernate with multi-tenancy support.
 * <p>
 * This class defines beans for JPA configuration, including entity manager factory,
 * tenant identifier resolver, and Hibernate vendor adapter.
 * </p>
 */
@Configuration
public class HibernateConfig {

    /**
     * Logger for HibernateConfig class.
     */
    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);

    /**
     * The package name where JPA entities are located.
     */
    public static final String ENTITY_PACKAGE = "com.foodsquad.FoodSquad.model";

    /**
     * JPA properties loaded from application configuration.
     */
    private final JpaProperties jpaProperties;

    /**
     * Constructs a new {@link HibernateConfig} with specified JPA properties.
     *
     * @param jpaProperties the JPA properties configured in the application.
     */
    public HibernateConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    /**
     * Initializes the configuration after dependency injection is complete.
     * Logs that the HibernateConfig has been initialized.
     */
    @PostConstruct
    public void postConstruct() {
        logger.info("HibernateConfig initialized with entity package '{}'", ENTITY_PACKAGE);
    }

    /**
     * Creates and configures a {@link JpaVendorAdapter} for Hibernate.
     *
     * @return a configured {@link HibernateJpaVendorAdapter} instance.
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        logger.debug("Creating JpaVendorAdapter (HibernateJpaVendorAdapter) bean");
        return new HibernateJpaVendorAdapter();
    }

    /**
     * Creates and configures the {@link CurrentTenantIdentifierResolver} bean.
     * <p>
     * This bean resolves the current tenant identifier in a multi-tenant setup.
     * </p>
     *
     * @return a new instance of {@link CurrentTenantIdentifierResolverImpl}.
     */
    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        logger.debug("Creating CurrentTenantIdentifierResolver bean");
        return new CurrentTenantIdentifierResolverImpl();
    }

    /**
     * Configures and creates a {@link LocalContainerEntityManagerFactoryBean} for multi-tenancy.
     * <p>
     * This method sets up entity manager factory with tenant-specific configurations
     * and Hibernate JPA vendor adapter.
     * </p>
     *
     * @param dataSource                       the primary data source.
     * @param multiTenantConnectionProvider    the multi-tenant connection provider.
     * @param currentTenantIdentifierResolver  the current tenant identifier resolver.
     * @return a configured {@link LocalContainerEntityManagerFactoryBean}.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource,
            final MultiTenantConnectionProviderImpl multiTenantConnectionProvider,
            final CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        logger.info("Configuring LocalContainerEntityManagerFactoryBean");

        // Merge JPA properties with multi-tenant configurations
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        // Create and configure the entity manager factory
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(ENTITY_PACKAGE);
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(properties);

        logger.debug("LocalContainerEntityManagerFactoryBean configured with properties: {}", properties);

        return em;
    }
}
