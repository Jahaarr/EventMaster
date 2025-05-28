package com.eventmaster.agents;

import com.eventmaster.models.Event;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class PlanningAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("PlanningAgent traite la requête : " + request);
        if (request.contains("planifie") || request.contains("conférence")) {
            LocalDate date = LocalDate.now(); // Default to today if date not parsed
            try {
                if (request.contains("mois prochain")) {
                    // Définir la date au premier jour du mois suivant
                    date = LocalDate.now().plusMonths(1).withDayOfMonth(1);
                    System.out.println("Date définie pour le mois prochain : " + date);
                } else {
                    String[] parts = request.split("pour le ");
                    if (parts.length > 1) {
                        String dateString = parts[1].trim();
                        System.out.println("Extracted date string: " + dateString);
                        // Create a formatter for French date format "d MMMM"
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH);
                        // Parse the day and month
                        java.time.temporal.TemporalAccessor temporalAccessor = formatter.parse(dateString);
                        int dayOfMonth = Integer.parseInt(DateTimeFormatter.ofPattern("d").withLocale(Locale.FRENCH).format(temporalAccessor));
                        int month = Integer.parseInt(DateTimeFormatter.ofPattern("M").withLocale(Locale.FRENCH).format(temporalAccessor));
                        // Set the year to 2025 (current year)
                        date = LocalDate.of(2025, month, dayOfMonth);
                    }
                }
            } catch (DateTimeParseException | NumberFormatException e) {
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