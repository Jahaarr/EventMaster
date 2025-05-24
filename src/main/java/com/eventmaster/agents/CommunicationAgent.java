package com.eventmaster.agents;

import org.springframework.stereotype.Component;

@Component
public class CommunicationAgent implements agent {
    @Override
    public String processRequest(String request) {
        if (request.contains("lance une campagne")) {
            return "Campagne de communication lancée sur Twitter, LinkedIn, et Instagram.";
        } else if (request.contains("inscriptions")) {
            return "Système d'inscription en ligne mis en place. 50 inscriptions reçues.";
        }
        return "Demande non reconnue par l'Agent de Communication.";
    }
}