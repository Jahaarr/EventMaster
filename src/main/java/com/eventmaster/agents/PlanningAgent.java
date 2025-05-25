package com.eventmaster.agents;

import com.eventmaster.models.Event;
import java.time.LocalDate;

public class PlanningAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("PlanningAgent traite la requête : " + request);
        if (request.contains("planifie") || request.contains("conférence")) {
            Event event = new Event();
            event.setId(1L);
            event.setName("Conférence IA");
            event.setTheme("Intelligence Artificielle");
            event.setDate(LocalDate.of(2025, 9, 15));
            event.setLocation("Centre de Congrès, Paris");
            event.setExpectedParticipants(200);
            event.setBudget(10000.0);
            return "Événement planifié : " + event.getName() + " le " + event.getDate() + " à " + event.getLocation();
        }
        return "Demande non reconnue par l'Agent de Planification.";
    }
}