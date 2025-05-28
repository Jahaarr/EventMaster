package com.eventmaster.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class EventCoordinator extends Agent {
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println("EventCoordinator a reçu un message : " + content);
                    ACLMessage reply = msg.createReply();

                    // Router la requête à l'agent approprié
                    if (content.toLowerCase().contains("planifie") || content.toLowerCase().contains("modifie les objectifs") ||
                            content.toLowerCase().contains("change la date") || content.toLowerCase().contains("change le lieu")) {
                        sendToAgent("PlanningAgent", content, reply);
                    } else if (content.toLowerCase().contains("recherche un intervenant") ||
                            content.toLowerCase().contains("confirme l'intervenant") ||
                            content.toLowerCase().contains("vérifie les besoins techniques")) {
                        sendToAgent("SpeakerCoordinationAgent", content, reply);
                    } else if (content.toLowerCase().contains("crée une campagne") ||
                            content.toLowerCase().contains("ouvre les inscriptions") ||
                            content.toLowerCase().contains("enregistre une inscription") ||
                            content.toLowerCase().contains("envoie le feedback")) {
                        sendToAgent("CommunicationAgent", content, reply);
                    } else if (content.toLowerCase().contains("coordonne les fournisseurs") ||
                            content.toLowerCase().contains("organise les transports")) {
                        sendToAgent("LogisticsAgent", content, reply);
                    } else {
                        reply.setPerformative(ACLMessage.FAILURE);
                        reply.setContent("Désolé, aucune réponse trouvée pour votre demande");
                        send(reply);
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void sendToAgent(String agentName, String content, ACLMessage reply) {
        System.out.println("Envoi de la requête à " + agentName + " : " + content);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
        msg.setContent(content);
        send(msg);

        // Attendre la réponse de l'agent
        ACLMessage response = blockingReceive(1000); // Timeout de 1 seconde
        if (response != null && response.getPerformative() == ACLMessage.INFORM) {
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContent("Réponse finale : " + response.getContent());
        } else {
            reply.setPerformative(ACLMessage.FAILURE);
            reply.setContent("Désolé, aucune réponse trouvée pour votre demande");
        }
        System.out.println("Envoi de la réponse finale : " + reply.getContent());
        send(reply);
    }
}