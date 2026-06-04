package com.example.demo.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ChatRequest;
import com.example.demo.utilisateur.service.DatabaseAgentService;
import com.example.demo.utilisateur.service.GeneralAgentService;

import com.example.demo.dto.ChatResponse;
import com.example.demo.utilisateur.service.RouterAgentService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    private final RouterAgentService routerAgentService;
    private final GeneralAgentService generalAgentService;
    private final DatabaseAgentService databaseAgentService;
    

    public ChatController(
            RouterAgentService routerAgentService,
            GeneralAgentService generalAgentService,
            DatabaseAgentService databaseAgentService
    ) {
        this.routerAgentService = routerAgentService;
        this.generalAgentService = generalAgentService;
        this.databaseAgentService = databaseAgentService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String userMessage = request.getMessage();
        Long clientId = request.getClientId();

        if (userMessage == null || userMessage.isBlank()) {
            return new ChatResponse("Message vide.", "ERROR");
        }

        String route = routerAgentService.detectIntent(userMessage);
        System.out.println("ROUTE = " + route);

        // =========================
        // GENERAL (IA)
        // =========================
        if ("GENERAL".equals(route)) {
            try {
                String response = generalAgentService.askGeneral(userMessage);

                if (response == null || response.isBlank()) {
                    return new ChatResponse(
                            "Je n'ai pas pu générer une réponse.",
                            "GENERAL"
                    );
                }

                return new ChatResponse(response, "GENERAL");

            } catch (Exception e) {
                System.out.println("GENERAL ERROR = " + e.getMessage());
                return new ChatResponse(
                        "Service IA indisponible pour le moment.",
                        "GENERAL"
                );
            }
        }

        // =========================
        // DATABASE
        // =========================
        try {
            String response = databaseAgentService.handle(userMessage, clientId);

            if (response == null || response.isBlank()) {
                response = "Aucune donnée trouvée.";
            }
            if ("DATABASE".equals(route) || userMessage.contains("payer") || userMessage.contains("combien")) {
    return new ChatResponse(databaseAgentService.handle(userMessage, clientId), "DATABASE");
}

            return new ChatResponse(response, "DATABASE");

        } catch (Exception e) {
            System.out.println("DATABASE ERROR = " + e.getMessage());
            return new ChatResponse(
                    "Erreur lors de la récupération des données.",
                    "DATABASE"
            );
        }
    }
}
