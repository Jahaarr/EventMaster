package com.eventmaster.coordinator;

import com.eventmaster.agents.agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventCoordinator {
    private final List<agent> agents;

    @Autowired
    public EventCoordinator(List<agent> agents) {
        this.agents = agents;
    }

    public String processUserRequest(String request) {
        for (agent agent : agents) {
            String response = agent.processRequest(request);
            if (!response.contains("non reconnue")) {
                return response;
            }
        }
        return "Désolé, aucune réponse trouvée pour votre demande.";
    }
}