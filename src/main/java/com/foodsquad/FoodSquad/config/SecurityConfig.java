package com.foodsquad.FoodSquad.config;

import com.foodsquad.FoodSquad.filter.JwtRequestFilter;
import com.foodsquad.FoodSquad.exception.CustomAccessDeniedHandler;
import com.foodsquad.FoodSquad.exception.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;


    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomAccessDeniedHandler customAccessDeniedHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {

        this.jwtRequestFilter = jwtRequestFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/token/**").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/media/**").permitAll()
                                .requestMatchers(HttpMethod.GET , "/uploads/**").permitAll()
                                .requestMatchers(HttpMethod.GET , "/images/**").permitAll()


                                .requestMatchers(HttpMethod.GET, "/api/invoice/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/invoice/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/currency/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "MODERATOR", "NORMAL")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/promotions/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/promotions/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/promotions/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/promotions/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/promotions/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/orders/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/orders/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/orders/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/orders/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/timbres/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/timbres/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/timbres/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/timbres/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/menu-items/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/menu-items/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/menu-items/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/menu-items/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/reviews/**").hasAnyRole("ADMIN", "MODERATOR", "NORMAL")
                                .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasAnyRole("ADMIN", "MODERATOR", "NORMAL")
                                .requestMatchers(HttpMethod.PUT, "/api/reviews/**").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/items-promotions/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/items-promotions/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/items-promotions/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/category-promotions/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/category-promotions/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/category-promotions/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/category-promotions/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/stores/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/stores/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/stores/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/stores/**").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(customAccessDeniedHandler)
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

}
