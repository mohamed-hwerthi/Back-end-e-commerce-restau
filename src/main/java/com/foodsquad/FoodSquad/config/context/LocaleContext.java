package com.foodsquad.FoodSquad.config.context;


import org.springframework.stereotype.Component;

/**
 * Holds the current request's locale in a ThreadLocal variable.
 * Allows access to the locale from any service, mapper, or component during request processing.
 */
@Component
public class LocaleContext {

    private static final ThreadLocal<String> CURRENT_LOCALE = new ThreadLocal<>();

    public String getLocale() {
        return CURRENT_LOCALE.get();
    }

    public void setLocale(String locale) {
        CURRENT_LOCALE.set(locale);
    }

    public void clear() {
        CURRENT_LOCALE.remove();
    }
}
