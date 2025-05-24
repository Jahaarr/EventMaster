package com.eventmaster.agents;

import com.eventmaster.models.Speaker;
import org.springframework.stereotype.Component;

@Component
public class SpeakerCoordinationAgent implements agent {
    @Override
    public String processRequest(String request) {
        if (request.contains("intervenants disponibles")) {
            Speaker speaker = new Speaker();
            speaker.setId(1L);                        // Error: cannot find symbol setId
            speaker.setName("Dr. Dupont");            // Error: cannot find symbol setName
            speaker.setExpertise("Apprentissage Profond"); // Error: cannot find symbol setExpertise
            speaker.setConfirmed(false);              // Error: cannot find symbol setConfirmed
            speaker.setTechnicalNeeds("Projecteur, micro"); // Error: cannot find symbol setTechnicalNeeds
            return "Intervenant trouvé : " + speaker.getName() + " (" + speaker.getExpertise() + ")";
            // Errors: cannot find symbols getName, getExpertise
        }
        return "Demande non reconnue par l'Agent de Coordination des Intervenants.";
    }
}