package com.eventmaster.agents;

public class LogisticsAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        if (request.contains("coordonne les fournisseurs")) {
            return "Fournisseurs coordonnés.";
        } else if (request.contains("organise les transports")) {
            String[] parts = request.split("pour les ");
            String target = parts.length > 1 ? parts[1].trim() : "participants";
            return "Transports organisés pour les " + target + ".";
        }
        return "Demande non reconnue par l'Agent Logistique.";
    }
}