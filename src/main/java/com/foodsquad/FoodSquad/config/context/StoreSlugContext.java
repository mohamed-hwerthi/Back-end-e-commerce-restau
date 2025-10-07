package com.foodsquad.FoodSquad.config.context;


/**
 * Holds the store slug for the current request context.
 * <p>
 * This allows services to access the slug directly without passing it explicitly.
 * </p>
 */
public class StoreSlugContext {

    private static final ThreadLocal<String> CURRENT_STORE_SLUG = new ThreadLocal<>();

    private StoreSlugContext() {
    }

    /**
     * Gets the current store slug.
     *
     * @return the store slug, or {@code null} if not set
     */
    public static String getCurrentStoreSlug() {
        return CURRENT_STORE_SLUG.get();
    }

    /**
     * Sets the current store slug.
     *
     * @param slug the store slug
     */
    public static void setCurrentStoreSlug(String slug) {
        CURRENT_STORE_SLUG.set(slug);
    }

    /**
     * Clears the current store slug.
     */
    public static void clear() {
        CURRENT_STORE_SLUG.remove();
    }
}
