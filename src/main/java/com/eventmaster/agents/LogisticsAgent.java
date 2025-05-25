package com.eventmaster.agents;

public class LogisticsAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("LogisticsAgent traite la requête : " + request);
        if (request.contains("vérifié les fournisseurs")) {
            return "Fournisseurs confirmés : traiteur réservé pour 200 personnes, matériel audiovisuel prêt.";
        } else if (request.contains("organise le transport")) {
            return "Transport organisé pour les intervenants : navette réservée depuis l'aéroport.";
        }
        return "Demande non reconnue par l'Agent Logistique.";
    }
}