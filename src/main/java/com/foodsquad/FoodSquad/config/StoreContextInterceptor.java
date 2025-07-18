package com.foodsquad.FoodSquad.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class StoreContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        String uri = request.getRequestURI();



        String storeId = request.getHeader("storeId");
        if (storeId == null) {
            storeId = request.getParameter("storeId");
        }

        if (!ObjectUtils.isEmpty(storeId)) {
            StoreContextHolder.setStoreId(storeId);
        } else {
            throw new IllegalStateException("StoreId cannot be null");
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        StoreContextHolder.clear();
    }
}
