package com.eventmaster.ui;

import com.eventmaster.coordinator.EventCoordinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {
    private final EventCoordinator coordinator;

    @Autowired
    public ConsoleUI(EventCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue dans EventMaster ! Entrez votre demande (ou 'quit' pour quitter) :");
        while (true) {
            System.out.print("> ");
            String request = scanner.nextLine();
            if (request.equalsIgnoreCase("quit")) {
                break;
            }
            String response = coordinator.processUserRequest(request);
            System.out.println(response);
        }
        scanner.close();
    }
}