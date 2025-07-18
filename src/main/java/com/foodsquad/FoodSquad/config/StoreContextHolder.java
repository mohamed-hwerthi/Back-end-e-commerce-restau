package com.foodsquad.FoodSquad.config;

public class StoreContextHolder {

    private static final ThreadLocal<String> currentStore = new ThreadLocal<>();

    public static void setStoreId(String storeId) {
        currentStore.set(storeId);
    }

    public static String getStoreId() {
        return currentStore.get();
    }

    public static void clear() {
        currentStore.remove();
    }
}
