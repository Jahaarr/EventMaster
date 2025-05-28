package com.eventmaster.agents;

import com.eventmaster.models.Speaker;
import java.util.HashMap;
import java.util.Map;

public class SpeakerCoordinationAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("SpeakerCoordinationAgent traite la requête : " + request);
        if (request.toLowerCase().contains("recherche un intervenant")) {
            String[] parts = request.split("nommé ");
            String name = parts.length > 1 ? parts[1].trim().split(" ")[0] : "Inconnu";
            return "Intervenant trouvé : " + name;
        }
        return "Demande non reconnue par l'Agent de Coordination des Intervenants.";
    }
}