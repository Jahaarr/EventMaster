package com.eventmaster;

import com.eventmaster.agents.RestAgentBridge;
import jakarta.annotation.PostConstruct;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringEventMasterApplication {
    private AgentContainer container;

    public static void main(String[] args) {
        SpringApplication.run(SpringEventMasterApplication.class, args);
    }

    @PostConstruct
    public void initJade() {
        try {
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN_HOST, "localhost");
            container = rt.createMainContainer(p);

            // Start JADE agents
            AgentController planningAgent = container.createNewAgent("PlanningAgent", "com.eventmaster.agents.PlanningAgent", null);
            AgentController speakerAgent = container.createNewAgent("SpeakerCoordinationAgent", "com.eventmaster.agents.SpeakerCoordinationAgent", null);
            AgentController communicationAgent = container.createNewAgent("CommunicationAgent", "com.eventmaster.agents.CommunicationAgent", null);
            AgentController logisticsAgent = container.createNewAgent("LogisticsAgent", "com.eventmaster.agents.LogisticsAgent", null);
            AgentController coordinatorController = container.createNewAgent("EventCoordinator", "com.eventmaster.coordinator.EventCoordinator", null);
            AgentController restAgentBridge = container.createNewAgent("RestAgentBridge", "com.eventmaster.agents.RestAgentBridge", null);

            planningAgent.start();
            speakerAgent.start();
            communicationAgent.start();
            logisticsAgent.start();
            coordinatorController.start();
            restAgentBridge.start();

            // Wait for agents to be ready
            boolean allAgentsReady = false;
            int attempts = 0;
            while (!allAgentsReady && attempts < 10) {
                try {
                    Thread.sleep(1000); // Wait 1 second per attempt
                    container.getAgent("EventCoordinator");
                    container.getAgent("PlanningAgent");
                    container.getAgent("SpeakerCoordinationAgent");
                    container.getAgent("CommunicationAgent");
                    container.getAgent("LogisticsAgent");
                    container.getAgent("RestAgentBridge");
                    allAgentsReady = true;
                    System.out.println("All JADE agents started successfully at " + java.time.LocalDateTime.now());
                } catch (Exception e) {
                    attempts++;
                    System.out.println("Waiting for agents... Attempt " + attempts + ": " + e.getMessage());
                }
            }

            if (!allAgentsReady) {
                throw new RuntimeException("Some agents are not ready after multiple attempts");
            }
        } catch (Exception e) {
            System.err.println("Error initializing JADE at " + java.time.LocalDateTime.now() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize JADE", e);
        }
    }

    @Bean
    public AgentContainer jadeAgentContainer() {
        if (container == null) {
            throw new IllegalStateException("AgentContainer is not initialized");
        }
        return container;
    }

    // Remove or comment out the restAgentBridge bean for now if using the alternative approach
    // @Bean
    // public RestAgentBridge restAgentBridge() {
    //     // Existing logic...
    // }
}