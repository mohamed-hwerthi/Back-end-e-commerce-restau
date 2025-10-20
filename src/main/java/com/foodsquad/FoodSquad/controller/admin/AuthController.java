package com.foodsquad.FoodSquad.controller.admin;

import com.foodsquad.FoodSquad.model.dto.AuthResponseDTO;
import com.foodsquad.FoodSquad.model.dto.UserLoginDTO;
import com.foodsquad.FoodSquad.model.dto.UserRegistrationDTO;
import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.service.admin.impl.AuthService;
import com.foodsquad.FoodSquad.service.admin.impl.TokenService;
import com.foodsquad.FoodSquad.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private static final String COOKIE_PATH = "/";
    private static final String SAME_SITE_POLICY = "None";

    private final AuthService authService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public AuthController(AuthService authService, TokenService tokenService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
    }


    /**
     * ✅ Unified Sign-In Endpoint (for all roles)
     */
    @Operation(summary = "User sign-in (single endpoint for all roles)")
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, Object>> signIn(
            @Valid @RequestBody UserLoginDTO loginDTO,
            HttpServletResponse response) {

        logger.info("Login attempt for: {}", loginDTO.getEmail());

        AuthResponseDTO authResponse = authService.signIn(loginDTO);
        Map<String, Object> claims = authService.buildClaims(authResponse);

        String accessToken = jwtUtil.generateToken(claims, authResponse.getEmail(), accessTokenExpiration);
        String refreshToken = jwtUtil.generateToken(claims, authResponse.getEmail(), refreshTokenExpiration);

        tokenService.saveTokens(authResponse.getEmail(), accessToken, refreshToken);
        addRefreshTokenCookie(response, refreshToken);

        logger.info("Login successful for: {} with role {}", authResponse.getEmail(), authResponse.getRole());

        if (authResponse.getStoreId() != null) {
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "storeId", authResponse.getStoreId(),
                    "role", authResponse.getRole()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "role", authResponse.getRole()
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @CookieValue(REFRESH_TOKEN_COOKIE) String refreshToken,
            HttpServletResponse response) {

        String accessToken = extractBearerToken(authorizationHeader);
        if (accessToken == null) {
            return badRequest("Missing or invalid Authorization header");
        }

        try {
            jwtUtil.extractClaims(refreshToken);
        } catch (JwtException e) {
            return unauthorized("Invalid refresh token");
        }

        tokenService.invalidateTokens(accessToken, refreshToken);
        clearRefreshTokenCookie(response);

        return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String accessToken = extractBearerToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (accessToken == null) {
            return unauthorized("Missing or invalid Authorization header");
        }

        try {
            String email = jwtUtil.extractClaims(accessToken).getSubject();
            User user = authService.loadUserEntityByUsername(email);
            return ResponseEntity.ok(new UserResponseDTO(user));
        } catch (ExpiredJwtException e) {
            return unauthorized("Access token is expired");
        } catch (JwtException e) {
            return unauthorized("Failed to extract claims from access token");
        }
    }

    // ============= PRIVATE HELPERS =============

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH)
                .maxAge(refreshTokenExpiration / 1000)
                .sameSite(SAME_SITE_POLICY)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(true)
                .path(COOKIE_PATH)
                .maxAge(0)
                .sameSite(SAME_SITE_POLICY)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(AUTH_HEADER_PREFIX)) {
            return null;
        }
        return authorizationHeader.substring(AUTH_HEADER_PREFIX.length());
    }

    private ResponseEntity<Map<String, String>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", message));
    }

    private ResponseEntity<Map<String, String>> badRequest(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", message));
    }
}
