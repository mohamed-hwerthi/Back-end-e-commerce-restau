package com.foodsquad.FoodSquad.aop;

import com.foodsquad.FoodSquad.config.StoreContextHolder;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class StoreFilterAspect {

    private final EntityManager entityManager;



    public StoreFilterAspect(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @Before("@annotation(transactional)")
    public void enableStoreFilter(Transactional transactional) {
        String storeId = StoreContextHolder.getStoreId();

        if (storeId == null) {
            throw new IllegalStateException("storeId is not set in StoreContextHolder");
        }

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("storeFilter");
        filter.setParameter("storeId", storeId);
    }




}
