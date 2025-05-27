package com.eventmaster.agents;

public class CommunicationAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("CommunicationAgent traite la requête : " + request);
        if (request.contains("campagne de communication") && request.contains("réseaux sociaux")) {
            return "Campagne de communication lancée sur les réseaux sociaux : annonce publiée sur Twitter, LinkedIn et Instagram.";
        }
        return "Demande non reconnue par l'Agent de Communication.";
    }
}