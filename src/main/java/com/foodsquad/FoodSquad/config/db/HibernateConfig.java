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

@Configuration
public class HibernateConfig {

    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);

    public static final String ENTITY_PACKAGE = "com.foodsquad.FoodSquad.model";

    private final JpaProperties jpaProperties;

    public HibernateConfig(JpaProperties jpaProperties) {

        this.jpaProperties = jpaProperties;
    }

    @PostConstruct
    public void postConstruct() {

        logger.info("HibernateConfig initialized with entity package '{}'", ENTITY_PACKAGE);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {

        logger.debug("Creating JpaVendorAdapter (HibernateJpaVendorAdapter) bean");
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {

        logger.debug("Creating CurrentTenantIdentifierResolver bean");
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final DataSource dataSource,
            final MultiTenantConnectionProviderImpl multiTenantConnectionProvider,
            final CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

        logger.info("Configuring LocalContainerEntityManagerFactoryBean");

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(ENTITY_PACKAGE);
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaPropertyMap(properties);

        logger.debug("LocalContainerEntityManagerFactoryBean configured with properties: {}", properties);

        return em;
    }

}
