package com.eventmaster.agents;

import com.eventmaster.models.Event;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PlanningAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("PlanningAgent traite la requête : " + request);
        if (request.contains("planifie") || request.contains("conférence")) {
            LocalDate date = LocalDate.now(); // Default to today if date not parsed
            try {
                String[] parts = request.split("pour le ");
                if (parts.length > 1) {
                    date = LocalDate.parse(parts[1].trim()); // Parse the date from the request
                }
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse date from request, using default: " + e.getMessage());
            }

            Event event = new Event();
            event.setId(1L);
            event.setName("Conférence IA");
            event.setTheme("Intelligence Artificielle");
            event.setDate(date);
            event.setLocation("Centre de Congrès, Paris");
            event.setExpectedParticipants(200);
            event.setBudget(10000.0);
            return "Événement planifié : " + event.getName() + " le " + event.getDate() + " à " + event.getLocation();
        }
        return "Demande non reconnue par l'Agent de Planification.";
    }
}