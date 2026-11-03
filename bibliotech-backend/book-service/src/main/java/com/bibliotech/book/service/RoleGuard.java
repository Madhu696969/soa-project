package com.bibliotech.book.service;

import com.bibliotech.book.exception.AccessDeniedCustomException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The API Gateway validates the JWT and forwards the caller's role via the
 * X-User-Role header. Business services trust this header as the gateway is
 * the only network entry point in this architecture.
 */
@Component
public class RoleGuard {

    public void require(String actualRole, List<String> allowedRoles) {
        if (actualRole == null || !allowedRoles.contains(actualRole)) {
            throw new AccessDeniedCustomException("Access denied: requires one of " + allowedRoles);
        }
    }
}
