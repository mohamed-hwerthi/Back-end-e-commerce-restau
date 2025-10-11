package com.foodsquad.FoodSquad.config.context;

import org.springframework.stereotype.Component;

/**
 * Holds the current request's locale in a ThreadLocal variable.
 * Allows access to the locale from any service, mapper, or component during request processing.
 */
@Component
public class LocaleContext {

    private static final ThreadLocal<String> CURRENT_LOCALE = new ThreadLocal<>();

    /** Static access (for mappers and utility classes) **/
    public static String get() {
        return CURRENT_LOCALE.get();
    }

    /** Static setter (for filters/interceptors before mapping) **/
    public static void set(String locale) {
        CURRENT_LOCALE.set(locale);
    }

    /** Static cleaner (for end of request) **/
    public static void clear() {
        CURRENT_LOCALE.remove();
    }

    /** Optional instance-level access (for beans) **/
    public String getLocale() {
        return CURRENT_LOCALE.get();
    }

    public void setLocale(String locale) {
        CURRENT_LOCALE.set(locale);
    }

    public void clearLocale() {
        CURRENT_LOCALE.remove();
    }
}
