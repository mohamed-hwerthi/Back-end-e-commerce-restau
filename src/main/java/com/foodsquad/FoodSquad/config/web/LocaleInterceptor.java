    package com.foodsquad.FoodSquad.config.web;

    import com.foodsquad.FoodSquad.config.context.LocaleContext;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.stereotype.Component;
    import org.springframework.web.servlet.HandlerInterceptor;

    /**
     * Interceptor that reads the "Accept-Language" or custom header from the request
     * and stores it in the {@link LocaleContext}.
     */
    @Component
    public class LocaleInterceptor implements HandlerInterceptor {

        private final LocaleContext localeContext;

        public LocaleInterceptor(LocaleContext localeContext) {
            this.localeContext = localeContext;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            String locale = request.getHeader("locale");
            if (locale == null || locale.isBlank()) {
                locale = request.getLocale().getLanguage();
            }

            localeContext.setLocale(locale);
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request,  HttpServletResponse response, Object handler, Exception ex) {
            localeContext.clear();
        }
    }

