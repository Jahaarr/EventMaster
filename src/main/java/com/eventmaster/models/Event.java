package com.eventmaster.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Event {
    @Id
    private Long id;
    private String name;
    private String theme;
    private LocalDate date;
    private String location;
    private int expectedParticipants;
    private double budget;

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getExpectedParticipants() {
        return expectedParticipants;
    }

    public double getBudget() {
        return budget;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExpectedParticipants(int expectedParticipants) {
        this.expectedParticipants = expectedParticipants;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}