package com.eventmaster.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Speaker {
    @Id
    private Long id;
    private String name;
    private String expertise;
    private boolean confirmed;
    private String technicalNeeds;

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpertise() {
        return expertise;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getTechnicalNeeds() {
        return technicalNeeds;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setTechnicalNeeds(String technicalNeeds) {
        this.technicalNeeds = technicalNeeds;
    }
}