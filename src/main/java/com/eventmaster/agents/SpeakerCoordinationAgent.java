package com.eventmaster.agents;

import com.eventmaster.models.Speaker;

public class SpeakerCoordinationAgent extends BaseAgent {
    @Override
    public String processRequest(String request) {
        System.out.println("SpeakerCoordinationAgent traite la requête : " + request);
        if (request.contains("intervenants disponibles")) {
            Speaker speaker = new Speaker();
            speaker.setId(1L);
            speaker.setName("Dr. Dupont");
            speaker.setExpertise("Apprentissage Profond");
            speaker.setConfirmed(false);
            speaker.setTechnicalNeeds("Projecteur, micro");
            return "Intervenant trouvé : " + speaker.getName() + " (" + speaker.getExpertise() + ")";
        }
        return "Demande non reconnue par l'Agent de Coordination des Intervenants.";
    }
}