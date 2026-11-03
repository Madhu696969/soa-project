package com.bibliotech.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Validates JWT on all requests except public auth endpoints and forwards
 * user identity to downstream services via headers (X-User-Id, X-User-Role, X-User-Email).
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String SECRET = "bibliotech-super-secret-key-for-jwt-signing-must-be-long-enough-256bits";

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (isPublic(path)) {
            return chain.filter(exchange);
        }
        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            return onError(exchange, "Missing or invalid Authorization header");
        }
        String token = authHeaders.get(0).substring(7);
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
            var claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token).getPayload();

            Object userIdClaim = claims.get("userId");
            Object roleClaim = claims.get("role");
            String email = claims.getSubject();

            if (userIdClaim == null || roleClaim == null || email == null || email.isBlank()) {
                return onError(exchange, "JWT token missing required claims");
            }

            String userId = userIdClaim.toString();
            String role = roleClaim.toString();

            // Overwrite (never append) so a client cannot spoof its own identity headers
            ServerHttpRequest mutatedRequest = request.mutate()
                    .headers(h -> {
                        h.remove("X-User-Id");
                        h.remove("X-User-Role");
                        h.remove("X-User-Email");
                    })
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .header("X-User-Email", email)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return onError(exchange, "Invalid or expired JWT token");
        }
    }

    private boolean isPublic(String path) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(p -> path.equals(p) || path.startsWith(p + "/"));
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        DataBufferFactory bufferFactory = response.bufferFactory();
        String body = "{\"message\": \"" + message + "\"}";
        DataBuffer buffer = bufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
