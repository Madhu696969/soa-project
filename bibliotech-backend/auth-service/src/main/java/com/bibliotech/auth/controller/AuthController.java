package com.bibliotech.auth.controller;
import com.bibliotech.auth.dto.*;
import com.bibliotech.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    /**
     * Called by the Gateway which injects X-User-Email after validating the JWT.
     * Also callable directly by other microservices (Feign) to resolve a user by id.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@RequestHeader("X-User-Email") String email) {
        return ResponseEntity.ok(authService.getUserByEmail(email));
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }
}
