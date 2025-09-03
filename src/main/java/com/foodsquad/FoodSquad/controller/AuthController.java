package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.StoreOwnerAuthResponse;
import com.foodsquad.FoodSquad.model.dto.UserLoginDTO;
import com.foodsquad.FoodSquad.model.dto.UserRegistrationDTO;
import com.foodsquad.FoodSquad.model.dto.UserResponseDTO;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.service.impl.AuthService;
import com.foodsquad.FoodSquad.service.impl.TokenService;
import com.foodsquad.FoodSquad.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Authentication Controller
 *
 * <p>Handles user and store owner authentication including:
 * - User registration
 * - User login
 * - Cashier login
 * - Store owner login
 * - Logout
 * - Fetching current authenticated user</p>
 */
@Tag(name = "2. Authentication", description = "Authentication API")
@Validated
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

    // ============================
    // User Registration
    // ============================
    @Operation(summary = "User registration", description = "Register a new user with the provided registration details.")
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {

        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            return badRequest("Passwords do not match");
        }

        UserResponseDTO user = authService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok(user);
    }

    // ============================
    // User Login
    // ============================
    @Operation(summary = "User login", description = "Authenticate a user with the provided login credentials.")
    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, String>> loginUser(
            @Parameter(description = "User login details", required = true)
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletResponse response) {

        UserResponseDTO user = authService.loginUser(userLoginDTO);
        return generateTokenResponse(user, response);
    }

    // ============================
    // Cashier Login
    // ============================
    @Operation(summary = "Cashier login", description = "Authenticate a cashier or admin user and issue JWT tokens.")
    @PostMapping("/cashier/sign-in")
    public ResponseEntity<Map<String, String>> loginCashier(
            @Parameter(description = "Cashier login details", required = true)
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletResponse response) {

        UserResponseDTO user = authService.loginCashier(userLoginDTO);
        return generateTokenResponse(user, response);
    }

    // ============================
    // Store Owner Login
    // ============================
    @Operation(
            summary = "Store owner login",
            description = "Authenticates a store owner using email and password and returns store details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                                content = @Content(schema = @Schema(implementation = StoreOwnerAuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content)
            }
    )
    @PostMapping("/owner/sign-in")
    // Logout
    public ResponseEntity<Map<String, String>> loginStoreOwner(
            @Valid @RequestBody UserLoginDTO loginDTO) {

        StoreOwnerAuthResponse response = authService.loginStoreOwner(loginDTO);
        return generateTokenResponse(response);
    }

    // ============================
    // Logout
    // ============================
    @Operation(summary = "Logout user", description = "Invalidate the refresh token and logout the user.")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(
            @Parameter(description = "Access token stored in header", required = true)
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @Parameter(description = "Refresh token stored in cookie", required = true)
            @CookieValue(REFRESH_TOKEN_COOKIE) String refreshToken,
            HttpServletResponse response) {

        String accessToken = extractBearerToken(authorizationHeader);
        if (accessToken == null) return badRequest("Missing or invalid Authorization header");

        try {
            jwtUtil.extractClaims(refreshToken);
        } catch (JwtException e) {
            logger.error("Invalid refresh token during logout: {}", e.getMessage());
            return unauthorized("Invalid refresh token");
        }

        tokenService.invalidateTokens(accessToken, refreshToken);
        clearRefreshTokenCookie(response);

        return ResponseEntity.ok(Map.of("message", "Successfully logged out"));
    }

    // ============================
    // Get Current User
    // ============================
    @Operation(summary = "Get current user", description = "Get the details of the currently authenticated user.")
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String accessToken = extractBearerToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (accessToken == null) return unauthorized("Missing or invalid Authorization header");

        String email;
        try {
            email = jwtUtil.extractClaims(accessToken).getSubject();
        } catch (ExpiredJwtException e) {
            logger.warn("Expired access token: {}", e.getMessage());
            return unauthorized("Access token is expired");
        } catch (JwtException e) {
            logger.error("Error extracting claims from access token: {}", e.getMessage());
            return unauthorized("Failed to extract claims from access token");
        }

        User user = authService.loadUserEntityByUsername(email);
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    // ============================
    // Private Helpers
    // ============================
    private ResponseEntity<Map<String, String>> generateTokenResponse(UserResponseDTO user, HttpServletResponse response) {
        Map<String, Object> claims = buildClaims(user);
        String accessToken = jwtUtil.generateToken(claims, user.getEmail(), accessTokenExpiration);
        String refreshToken = jwtUtil.generateToken(claims, user.getEmail(), refreshTokenExpiration);

        tokenService.saveTokens(user.getEmail(), accessToken, refreshToken);
        addRefreshTokenCookie(response, refreshToken);

        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    private ResponseEntity<Map<String, String>> generateTokenResponse(StoreOwnerAuthResponse storeOwner) {
        Map<String, Object> claims = buildClaims(storeOwner);
        String accessToken = jwtUtil.generateToken(claims, storeOwner.getEmail(), accessTokenExpiration);
        String refreshToken = jwtUtil.generateToken(claims, storeOwner.getEmail(), refreshTokenExpiration);

        tokenService.saveTokens(storeOwner.getEmail(), accessToken, refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    private Map<String, Object> buildClaims(UserResponseDTO user) {
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "imageUrl", user.getImageUrl(),
                "phoneNumber", user.getPhoneNumber()
        );
    }

    private Map<String, Object> buildClaims(StoreOwnerAuthResponse storeOwner) {
        return Map.of(
                "id", storeOwner.getId(),
                "email", storeOwner.getEmail(),
                "storeId", storeOwner.getStoreId(),
                "role", UserRole.OWNER
        );
    }

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
