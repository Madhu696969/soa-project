package com.bibliotech.book.service;

import com.bibliotech.book.exception.AccessDeniedCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Role-based authorization tests. The gateway forwards the caller's role in
 * X-User-Role; RoleGuard is what turns that into an allow/deny decision.
 */
class RoleGuardTest {

    private final RoleGuard roleGuard = new RoleGuard();

    private static final List<String> MANAGE_BOOKS = List.of("ADMIN", "LIBRARIAN");

    @Test
    @DisplayName("ADMIN may manage books")
    void adminIsAllowedToManageBooks() {
        assertDoesNotThrow(() -> roleGuard.require("ADMIN", MANAGE_BOOKS));
    }

    @Test
    @DisplayName("LIBRARIAN may manage books")
    void librarianIsAllowedToManageBooks() {
        assertDoesNotThrow(() -> roleGuard.require("LIBRARIAN", MANAGE_BOOKS));
    }

    @Test
    @DisplayName("STUDENT may not manage books -> access denied")
    void studentIsDeniedBookManagement() {
        assertThrows(AccessDeniedCustomException.class,
                () -> roleGuard.require("STUDENT", MANAGE_BOOKS));
    }

    @Test
    @DisplayName("FACULTY may not manage books -> access denied")
    void facultyIsDeniedBookManagement() {
        assertThrows(AccessDeniedCustomException.class,
                () -> roleGuard.require("FACULTY", MANAGE_BOOKS));
    }

    @Test
    @DisplayName("A missing role header is rejected rather than treated as trusted")
    void nullRoleIsDenied() {
        assertThrows(AccessDeniedCustomException.class,
                () -> roleGuard.require(null, MANAGE_BOOKS));
    }

    @Test
    @DisplayName("An unrecognised role is rejected")
    void unknownRoleIsDenied() {
        assertThrows(AccessDeniedCustomException.class,
                () -> roleGuard.require("SUPERUSER", MANAGE_BOOKS));
    }

    @Test
    @DisplayName("Rental Service's internal SYSTEM role may change copy status")
    void systemRoleMayUpdateCopyStatus() {
        List<String> updateCopyStatus = List.of("ADMIN", "LIBRARIAN", "SYSTEM");
        assertDoesNotThrow(() -> roleGuard.require("SYSTEM", updateCopyStatus));
    }
}
