package com.eventmaster.agents;

public class CommunicationAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("CommunicationAgent traite la requête : " + request);
        if (request.contains("lance une campagne")) {
            return "Campagne de communication lancée sur Twitter, LinkedIn, et Instagram.";
        } else if (request.contains("inscriptions")) {
            return "Système d'inscription en ligne mis en place. 50 inscriptions reçues.";
        }
        return "Demande non reconnue par l'Agent de Communication.";
    }
}