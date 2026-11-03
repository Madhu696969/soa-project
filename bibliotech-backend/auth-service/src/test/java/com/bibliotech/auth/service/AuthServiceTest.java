package com.bibliotech.auth.service;

import com.bibliotech.auth.dto.LoginRequest;
import com.bibliotech.auth.dto.RegisterRequest;
import com.bibliotech.auth.entity.Role;
import com.bibliotech.auth.entity.User;
import com.bibliotech.auth.exception.DuplicateEmailException;
import com.bibliotech.auth.exception.InvalidCredentialsException;
import com.bibliotech.auth.repository.UserRepository;
import com.bibliotech.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Alex Student");
        sampleUser.setEmail("student@bibliotech.com");
        sampleUser.setPassword("hashed-password");
        sampleUser.setRole(Role.STUDENT);
    }

    @Test
    void register_shouldCreateUser_whenEmailNotTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alex Student");
        request.setEmail("student@bibliotech.com");
        request.setPassword("Student@123");
        request.setRole(Role.STUDENT);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(jwtUtil.generateToken(sampleUser)).thenReturn("mock-jwt-token");

        var response = authService.register(request);

        assertEquals("mock-jwt-token", response.getToken());
        assertEquals(Role.STUDENT, response.getUser().getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowConflict_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("student@bibliotech.com");
        request.setPassword("Student@123");
        request.setRole(Role.STUDENT);
        request.setName("Alex");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("student@bibliotech.com");
        request.setPassword("Student@123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(request.getPassword(), sampleUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(sampleUser)).thenReturn("mock-jwt-token");

        var response = authService.login(request);

        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("student@bibliotech.com", response.getUser().getEmail());
    }

    @Test
    void login_shouldThrowUnauthorized_whenPasswordWrong() {
        LoginRequest request = new LoginRequest();
        request.setEmail("student@bibliotech.com");
        request.setPassword("wrongpass");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches(request.getPassword(), sampleUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_shouldThrowUnauthorized_whenUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("nouser@bibliotech.com");
        request.setPassword("whatever");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
