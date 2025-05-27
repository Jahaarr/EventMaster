package com.eventmaster.agents;

import java.util.concurrent.CompletableFuture;

public interface RestAgentBridgeInterface {
    CompletableFuture<String> sendRequestToCoordinator(String request);
}