package com.eventmaster.ui;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;

import java.util.Scanner;

public class ConsoleUI extends Agent {
    @Override
    protected void setup() {
        System.out.println("Démarrage de ConsoleUI à " + java.time.LocalDateTime.now());
        try {
            // Utiliser le conteneur actuel de ConsoleUI
            AgentContainer container = getContainerController();

            // Créer tous les agents dans le conteneur actuel
            System.out.println("Création des agents...");
            AgentController planningAgent = container.createNewAgent("PlanningAgent", "com.eventmaster.agents.PlanningAgent", null);
            AgentController speakerAgent = container.createNewAgent("SpeakerCoordinationAgent", "com.eventmaster.agents.SpeakerCoordinationAgent", null);
            AgentController communicationAgent = container.createNewAgent("CommunicationAgent", "com.eventmaster.agents.CommunicationAgent", null);
            AgentController logisticsAgent = container.createNewAgent("LogisticsAgent", "com.eventmaster.agents.LogisticsAgent", null);
            AgentController coordinatorController = container.createNewAgent("EventCoordinator", "com.eventmaster.coordinator.EventCoordinator", null);

            // Démarrer les agents
            System.out.println("Démarrage des agents...");
            planningAgent.start();
            speakerAgent.start();
            communicationAgent.start();
            logisticsAgent.start();
            coordinatorController.start();

            // Attendre que les agents soient prêts
            boolean allAgentsReady = false;
            int attempts = 0;
            while (!allAgentsReady && attempts < 10) {
                try {
                    Thread.sleep(1000); // Attendre 1 seconde par tentative
                    container.getAgent("EventCoordinator");
                    container.getAgent("PlanningAgent");
                    container.getAgent("SpeakerCoordinationAgent");
                    container.getAgent("CommunicationAgent");
                    container.getAgent("LogisticsAgent");
                    allAgentsReady = true;
                    System.out.println("Tous les agents sont prêts après " + attempts + " tentatives à " + java.time.LocalDateTime.now());
                } catch (Exception e) {
                    attempts++;
                    System.out.println("Attente des agents... Tentative " + attempts + " : " + e.getMessage());
                }
            }

            if (!allAgentsReady) {
                System.err.println("Erreur : Certains agents ne sont pas prêts après plusieurs tentatives à " + java.time.LocalDateTime.now());
                doDelete();
                return;
            }

            // Tester la communication avec EventCoordinator
            System.out.println("Test de communication avec EventCoordinator à " + java.time.LocalDateTime.now());
            ACLMessage testMsg = new ACLMessage(ACLMessage.REQUEST);
            testMsg.addReceiver(new AID("EventCoordinator", AID.ISLOCALNAME));
            testMsg.setContent("test");
            send(testMsg);
            ACLMessage reply = blockingReceive(10000); // Augmenter à 10 secondes
            if (reply != null && reply.getPerformative() == ACLMessage.INFORM && reply.getContent().contains("Test reçu avec succès")) {
                System.out.println("Test réussi : EventCoordinator a répondu correctement à " + java.time.LocalDateTime.now());
            } else {
                System.err.println("Erreur : EventCoordinator n'a pas répondu correctement au message de test à " + java.time.LocalDateTime.now());
                System.err.println("Réponse reçue : " + (reply != null ? reply.getContent() : "Aucune réponse"));
                doDelete();
                return;
            }

            System.out.println("Agents démarrés, lancement de la console à " + java.time.LocalDateTime.now());
            startConsole();
        } catch (Exception e) {
            System.err.println("Exception lors du démarrage de ConsoleUI à " + java.time.LocalDateTime.now() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue dans EventMaster ! Entrez votre requête (ou 'exit' pour quitter) :");
        System.out.print("> ");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                try {
                    if (scanner.hasNextLine()) {
                        String request = scanner.nextLine();

                        if (request.equalsIgnoreCase("exit")) {
                            System.out.println("Arrêt de EventMaster à " + java.time.LocalDateTime.now());
                            doDelete();
                            return;
                        }

                        // Envoyer la requête à EventCoordinator
                        System.out.println("Envoi de la requête : " + request + " à " + java.time.LocalDateTime.now());
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(new AID("EventCoordinator", AID.ISLOCALNAME));
                        msg.setContent(request);
                        send(msg);

                        // Attendre la réponse
                        System.out.println("Attente de la réponse à " + java.time.LocalDateTime.now());
                        ACLMessage reply = blockingReceive(10000); // Augmenter à 10 secondes
                        if (reply != null) {
                            System.out.println("Réponse reçue : " + reply.getContent() + " à " + java.time.LocalDateTime.now());
                        } else {
                            System.out.println("Aucune réponse reçue dans le délai imparti à " + java.time.LocalDateTime.now());
                        }

                        // Afficher le prompt pour la prochaine requête
                        System.out.print("> ");
                        System.out.flush();
                    } else {
                        block();
                    }
                } catch (Exception e) {
                    System.err.println("Exception dans startConsole à " + java.time.LocalDateTime.now() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        // Pas besoin de créer un nouveau conteneur ici, cela sera géré par JADE
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        try {
            AgentContainer container = rt.createMainContainer(p);
            container.acceptNewAgent("ConsoleUI", new ConsoleUI()).start();
        } catch (Exception e) {
            System.err.println("Exception dans main à " + java.time.LocalDateTime.now() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}