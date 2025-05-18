package com.afrosofttech.spring_starter.util.constants;

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), EDITOR("ROLE_EDITOR");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
