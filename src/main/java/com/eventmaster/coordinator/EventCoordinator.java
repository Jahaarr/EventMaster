package com.eventmaster.coordinator;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
import java.util.Map;

public class EventCoordinator extends Agent {
    private Map<AID, String> agentResponses = new HashMap<>();
    private String finalResponse;
    private AID originalSender; // Pour conserver l'émetteur original

    @Override
    protected void setup() {
        System.out.println("EventCoordinator démarré et opérationnel. AID : " + getAID().getName() + " à " + java.time.LocalDateTime.now());
        try {
            addBehaviour(new CyclicBehaviour(this) {
                @Override
                public void action() {
                    try {
                        ACLMessage msg = receive();
                        if (msg != null) {
                            System.out.println("EventCoordinator a reçu un message de " + msg.getSender().getName() + " : " + msg.getContent() + " à " + java.time.LocalDateTime.now());
                            if (msg.getPerformative() == ACLMessage.REQUEST) {
                                String request = msg.getContent();
                                if (request.equals("test")) {
                                    ACLMessage reply = msg.createReply();
                                    reply.setPerformative(ACLMessage.INFORM);
                                    reply.setContent("Test reçu avec succès à " + java.time.LocalDateTime.now());
                                    send(reply);
                                    return;
                                }
                                originalSender = msg.getSender(); // Conserver l'émetteur original
                                System.out.println("Requête reçue : " + request + " à " + java.time.LocalDateTime.now());
                                processRequestAsync(request, msg);
                            } else if (msg.getPerformative() == ACLMessage.INFORM) {
                                String response = msg.getContent();
                                AID sender = msg.getSender();
                                System.out.println("Réponse reçue de " + sender.getName() + " : " + response + " à " + java.time.LocalDateTime.now());
                                agentResponses.put(sender, response);

                                if (agentResponses.size() == 4) { // 4 agents
                                    boolean foundValidResponse = false;
                                    for (Map.Entry<AID, String> entry : agentResponses.entrySet()) {
                                        if (!entry.getValue().contains("non reconnue")) {
                                            finalResponse = "Réponse finale : " + entry.getValue() + " à " + java.time.LocalDateTime.now();
                                            foundValidResponse = true;
                                            break;
                                        }
                                    }
                                    if (!foundValidResponse) {
                                        finalResponse = "Désolé, aucune réponse trouvée pour votre demande à " + java.time.LocalDateTime.now();
                                    }

                                    // Envoyer la réponse à l'émetteur original (ConsoleUI)
                                    System.out.println("Envoi de la réponse finale à " + originalSender.getName() + " : " + finalResponse);
                                    ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                                    reply.addReceiver(originalSender);
                                    reply.setContent(finalResponse);
                                    send(reply);

                                    agentResponses.clear();
                                    finalResponse = null;
                                    originalSender = null;
                                }
                            }
                        } else {
                            block();
                        }
                    } catch (Exception e) {
                        System.err.println("Exception dans EventCoordinator à " + java.time.LocalDateTime.now() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Exception lors du démarrage de EventCoordinator à " + java.time.LocalDateTime.now() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processRequestAsync(String request, ACLMessage originalMsg) {
        String[] agentNames = {"PlanningAgent", "SpeakerCoordinationAgent", "CommunicationAgent", "LogisticsAgent"};
        agentResponses.clear();

        // Envoyer la requête à tous les agents
        System.out.println("Envoi de la requête aux agents : " + request + " à " + java.time.LocalDateTime.now());
        for (String agentName : agentNames) {
            try {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
                msg.setContent(request);
                send(msg);
                System.out.println("Requête envoyée à " + agentName + " à " + java.time.LocalDateTime.now());
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi de la requête à " + agentName + " à " + java.time.LocalDateTime.now() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}