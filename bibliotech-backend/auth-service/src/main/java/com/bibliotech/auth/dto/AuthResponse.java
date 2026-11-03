package com.bibliotech.auth.dto;


public class AuthResponse {
    private String token;
    private UserDto user;

    public AuthResponse() {
    }

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponse that = (AuthResponse) o;
        return java.util.Objects.equals(token, that.token) &&
                java.util.Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(token, user);
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token=" + token + ", " + "user=" + user +
                '}';
    }
}
