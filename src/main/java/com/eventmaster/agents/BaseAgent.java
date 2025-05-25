package com.eventmaster.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class BaseAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " démarré.");
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        String request = msg.getContent();
                        System.out.println(getLocalName() + " traite la requête : " + request);
                        String response = processRequest(request);
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(response);
                        send(reply);
                    }
                } else {
                    block();
                }
            }
        });
    }

    public abstract String processRequest(String request);
}