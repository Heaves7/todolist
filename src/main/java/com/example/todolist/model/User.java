package com.example.todolist.model;

public class User {
    private String id;
    private String name;
    private Role role;
    private String companyId;

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec paramètres
    public User(String id, String name, Role role, String companyId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.companyId = companyId;
    }

    // Getters et Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public User orElse(Object o) {
        return this;
    }
}