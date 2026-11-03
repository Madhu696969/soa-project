package com.bibliotech.auth.dto;

import com.bibliotech.auth.entity.Role;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto that = (UserDto) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(name, that.name) &&
                java.util.Objects.equals(email, that.email) &&
                java.util.Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, email, role);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id + ", " + "name=" + name + ", " + "email=" + email + ", " + "role=" + role +
                '}';
    }
}
