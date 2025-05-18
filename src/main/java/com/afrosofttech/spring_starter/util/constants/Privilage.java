package com.afrosofttech.spring_starter.util.constants;

public enum Privilage {
    RESET_PASSWORD(1L, "RESET_PASSWORD"),
    ACCESS_ADMIN_PANEL(2L, "ACCESS_ADMIN_PANEL");

    private Long id;
    private String name;

    Privilage(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
