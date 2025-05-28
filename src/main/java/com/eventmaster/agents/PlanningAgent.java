package com.eventmaster.agents;

import com.eventmaster.models.Event;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PlanningAgent extends BaseAgent {
    private Map<String, Object> eventDetails = new HashMap<>();

    @Override
    public String processRequest(String request) {
        System.out.println("PlanningAgent traite la requête : " + request + " à " + java.time.LocalDateTime.now());
        String response = "Demande non reconnue par l'Agent de Planification.";

        if (request.contains("planifie") || request.contains("conférence")) {
            LocalDate date = LocalDate.now();
            try {
                if (request.contains("mois prochain")) {
                    date = LocalDate.now().plusMonths(1).withDayOfMonth(1);
                    System.out.println("Date définie pour le mois prochain : " + date);
                } else if (request.contains("pour le ")) {
                    String[] parts = request.split("pour le ");
                    if (parts.length > 1) {
                        String dateString = parts[1].trim().split(" et ")[0].trim(); // Prend la partie avant "et"
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH);
                        java.time.temporal.TemporalAccessor temporalAccessor = formatter.parse(dateString);
                        int dayOfMonth = Integer.parseInt(DateTimeFormatter.ofPattern("d").format(temporalAccessor));
                        int month = Integer.parseInt(DateTimeFormatter.ofPattern("M").format(temporalAccessor));
                        date = LocalDate.of(2025, month, dayOfMonth);
                    }
                }
            } catch (DateTimeParseException | NumberFormatException e) {
                System.out.println("Failed to parse date from request, using default: " + e.getMessage());
            }

            int participants = extractParticipants(request);
            String theme = extractTheme(request, "Intelligence Artificielle");
            eventDetails.put("date", date);
            eventDetails.put("participants", participants);
            eventDetails.put("theme", theme);

            String location = extractLocation(request, (String) eventDetails.getOrDefault("location", "Centre de Congrès, Paris"));
            eventDetails.put("location", location);

            double budget = calculateBudget(participants);
            eventDetails.put("budget", budget);

            Event event = new Event();
            event.setId(1L);
            event.setName("Conférence " + theme);
            event.setTheme(theme);
            event.setDate(date);
            event.setLocation(location);
            event.setExpectedParticipants(participants);
            event.setBudget(budget);
            response = String.format("Événement planifié : %s le %s à %s. Objectifs : %d participants, thème %s. Budget prévisionnel : %.2f €",
                    event.getName(), event.getDate(), event.getLocation(), participants, theme, budget);
        } else if (request.toLowerCase().contains("modifie les objectifs")) { // Insensible à la casse
            int participants = extractParticipants(request);
            String theme = extractTheme(request, (String) eventDetails.getOrDefault("theme", "Intelligence Artificielle"));
            eventDetails.put("participants", participants);
            eventDetails.put("theme", theme);
            response = String.format("Objectifs modifiés : %d participants, thème %s", participants, theme);
        } else if (request.contains("change la date") || request.contains("change le lieu")) {
            LocalDate newDate = extractDate(request, (LocalDate) eventDetails.getOrDefault("date", LocalDate.now()));
            String newLocation = extractLocation(request, (String) eventDetails.getOrDefault("location", "Centre de Congrès, Paris"));
            eventDetails.put("date", newDate);
            eventDetails.put("location", newLocation);
            response = String.format("Date et lieu modifiés : %s à %s", newDate, newLocation);
        }

        return response;
    }

    private int extractParticipants(String request) {
        String[] words = request.split(" ");
        for (String word : words) {
            if (word.matches("\\d+")) {
                return Integer.parseInt(word);
            }
        }
        return 200;
    }

    private String extractTheme(String request, String defaultTheme) {
        if (request.contains("sur ")) {
            String[] parts = request.split("sur ");
            if (parts.length > 1) {
                String themePart = parts[1].trim().split(" avec ")[0].trim(); // Prend avant "avec"
                // Nettoyer les articles comme "l'", "le", "la"
                String[] themeWords = themePart.split(" ");
                StringBuilder cleanedTheme = new StringBuilder();
                for (String word : themeWords) {
                    if (!word.matches("l'|le|la")) {
                        cleanedTheme.append(word).append(" ");
                    }
                }
                return cleanedTheme.toString().trim();
            }
        }
        return defaultTheme;
    }

    private String selectLocation(int participants) {
        if (participants <= 100) {
            return "Salle de Conférence, Paris";
        } else if (participants <= 300) {
            return "Centre de Congrès, Paris";
        } else {
            return "Palais des Congrès, Paris";
        }
    }

    private double calculateBudget(int participants) {
        return participants * 50 + 1000;
    }

    private LocalDate extractDate(String request, LocalDate defaultDate) {
        try {
            if (request.contains("mois prochain")) {
                return LocalDate.now().plusMonths(1).withDayOfMonth(1);
            } else if (request.contains("pour le ") || request.contains("change la date")) {
                String[] parts = request.split("(pour le |change la date pour le )");
                if (parts.length > 1) {
                    String dateString = parts[1].trim().split(" et ")[0].trim(); // Prend la partie avant "et"
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.FRENCH);
                    java.time.temporal.TemporalAccessor temporalAccessor = formatter.parse(dateString);
                    int dayOfMonth = Integer.parseInt(DateTimeFormatter.ofPattern("d").format(temporalAccessor));
                    int month = Integer.parseInt(DateTimeFormatter.ofPattern("M").format(temporalAccessor));
                    return LocalDate.of(2025, month, dayOfMonth);
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            System.out.println("Failed to parse new date, using default: " + e.getMessage());
        }
        return defaultDate;
    }

    private String extractLocation(String request, String defaultLocation) {
        try {
            if (request.contains("change le lieu") || request.contains("à ")) {
                String[] parts = request.split("(change le lieu à |à )");
                if (parts.length > 1) {
                    return parts[1].trim().split(" ")[0] + (parts[1].trim().split(" ").length > 1 ? " " + parts[1].trim().split(" ")[1] : "");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Failed to parse location, using default: " + e.getMessage());
        }
        return defaultLocation;
    }
}