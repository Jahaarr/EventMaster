package com.eventmaster.agents;

import org.springframework.stereotype.Component;

@Component
public class LogisticsAgent implements agent {
    @Override
    public String processRequest(String request) {
        if (request.contains("vérifié les fournisseurs")) {
            return "Fournisseurs confirmés : traiteur réservé pour 200 personnes, matériel audiovisuel prêt.";
        }
        return "Demande non reconnue par l'Agent Logistique.";
    }
}