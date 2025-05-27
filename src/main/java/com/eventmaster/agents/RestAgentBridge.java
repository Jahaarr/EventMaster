package com.eventmaster.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.concurrent.CompletableFuture;

public class RestAgentBridge extends Agent implements RestAgentBridgeInterface {
    @Override
    protected void setup() {
        System.out.println("RestAgentBridge started at " + java.time.LocalDateTime.now());
        try {
            // Register this agent as an O2A interface using the interface type
            registerO2AInterface(RestAgentBridgeInterface.class, this);
            System.out.println("RestAgentBridge O2A interface registered at " + java.time.LocalDateTime.now());

            // Add a ticker behavior to periodically log agent status
            addBehaviour(new TickerBehaviour(this, 5000) {
                @Override
                protected void onTick() {
                    System.out.println("RestAgentBridge is alive and operational at " + java.time.LocalDateTime.now());
                }
            });
        } catch (Exception e) {
            System.err.println("Error registering O2A interface in RestAgentBridge at " + java.time.LocalDateTime.now() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<String> sendRequestToCoordinator(String request) {
        CompletableFuture<String> futureResponse = new CompletableFuture<>();

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("EventCoordinator", AID.ISLOCALNAME));
        msg.setContent(request);

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try {
                    System.out.println("RestAgentBridge sending request to EventCoordinator: " + request + " at " + java.time.LocalDateTime.now());
                    send(msg);
                    ACLMessage reply = blockingReceive(10000); // 10 seconds
                    if (reply != null && reply.getPerformative() == ACLMessage.INFORM) {
                        futureResponse.complete(reply.getContent());
                    } else {
                        futureResponse.complete("No response received from EventCoordinator.");
                    }
                } catch (Exception e) {
                    System.err.println("Error in RestAgentBridge sendRequestToCoordinator at " + java.time.LocalDateTime.now() + ": " + e.getMessage());
                    futureResponse.completeExceptionally(e);
                }
            }
        });

        return futureResponse;
    }
}