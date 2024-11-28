package com.example.todolist.model;

public class Task {
    private String id;
    private String description;
    private String assignedToUserId;
    private String companyId;

    // Constructeur par défaut
    public Task() {
    }

    // Constructeur avec paramètres
    public Task(String id, String description, String assignedToUserId, String companyId) {
        this.id = id;
        this.description = description;
        this.assignedToUserId = assignedToUserId;
        this.companyId = companyId;
    }

    // Getters et Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(String assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}