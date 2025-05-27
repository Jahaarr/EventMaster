package com.eventmaster.agents;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin(origins = "*")
public class EventMasterRestController {
    private final AgentContainer agentContainer;

    @Autowired
    public EventMasterRestController(AgentContainer agentContainer) {
        this.agentContainer = agentContainer;
    }

    @PostMapping("/api/request")
    public ResponseEntity<?> handleRequest(@RequestBody RequestDTO requestDTO) {
        String request = requestDTO.getRequest();
        if (request == null || request.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseDTO("Request cannot be empty"));
        }

        try {
            AgentController agentController = agentContainer.getAgent("RestAgentBridge");
            RestAgentBridgeInterface restAgentBridge = (RestAgentBridgeInterface) agentController.getO2AInterface(RestAgentBridgeInterface.class);
            if (restAgentBridge == null) {
                throw new RuntimeException("RestAgentBridge O2A interface is null");
            }
            String response = restAgentBridge.sendRequestToCoordinator(request).get();
            return ResponseEntity.ok(new ResponseDTO(response));
        } catch (Exception e) {
            System.err.println("Error in EventMasterRestController at " + java.time.LocalDateTime.now() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseDTO("Error: " + e.getMessage()));
        }
    }

    public static class RequestDTO {
        private String request;

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }
    }

    public static class ResponseDTO {
        private String response;

        public ResponseDTO(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}