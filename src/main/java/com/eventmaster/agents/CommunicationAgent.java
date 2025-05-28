package com.eventmaster.agents;

public class CommunicationAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("CommunicationAgent traite la requête : " + request);
        if (request.toLowerCase().contains("crée une campagne")) {
            return "Campagne sur les réseaux sociaux créée.";
        }
        return "Demande non reconnue par l'Agent de Communication.";
    }
}