package com.example.demo.utilisateur.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class RouterAgentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FuzzyMatcher fuzzyMatcher;

    @Value("${groq.api.key}") private String groqApiKey;
    @Value("${groq.api.url}") private String groqUrl;
    @Value("${groq.model}") private String model;

    public RouterAgentService(FuzzyMatcher fuzzyMatcher) {
        this.fuzzyMatcher = fuzzyMatcher;
    }

    public String detectIntent(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "GENERAL";
        }

        // 1. Vérification rapide locale avec FuzzyMatcher
        String localIntent = detectIntentLocally(userMessage);
        if (localIntent != null) {
            return localIntent;
        }

        // 2. Fallback vers Groq si ambigu
        return detectIntentWithGroq(userMessage);
    }

    // ============ DÉTECTION LOCALE RAPIDE ============

    private String detectIntentLocally(String message) {
        // Si ça match une catégorie DB → DATABASE
        if (fuzzyMatcher.findBestDbCategory(message).isPresent()) {
            return "DATABASE";
        }
        
        // Si c'est un greeting/thanks/goodbye → GENERAL
        if (fuzzyMatcher.isGreeting(message) || 
            fuzzyMatcher.isThanks(message) || 
            fuzzyMatcher.isGoodbye(message)) {
            return "GENERAL";
        }
        
        // Si ça match une catégorie générale → GENERAL
        if (fuzzyMatcher.findBestGeneralCategory(message).isPresent()) {
            return "GENERAL";
        }
        
        return null; // Ambigu, passer à Groq
    }

    // ============ DÉTECTION VIA GROQ ============

    private String detectIntentWithGroq(String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                    Map.of("role", "system", "content", SYSTEM_PROMPT),
                    Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0,
                "max_completion_tokens", 10,
                "stream", false
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            String response = restTemplate.postForObject(
                groqUrl + "/chat/completions", request, String.class);
            
            return extractIntent(response);
        } catch (Exception e) {
            return "GENERAL";
        }
    }

    private String extractIntent(String groqResponse) {
        try {
            JsonNode root = objectMapper.readTree(groqResponse);
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.isEmpty()) return "GENERAL";
            
            String content = choices.get(0).path("message").path("content")
                .asText("").trim().toUpperCase();
            
            if (content.contains("DATABASE")) return "DATABASE";
            return "GENERAL";
        } catch (Exception e) {
            return "GENERAL";
        }
    }

    private static final String SYSTEM_PROMPT = """
Tu es un routeur intelligent pour l'application PressIN.
Classifie en UNE SEULE catégorie : GENERAL ou DATABASE

DATABASE = données personnelles du client (commandes, articles, prix, livraisons, profil)
GENERAL = questions sur les services, horaires, tarifs, fonctionnement

Réponds uniquement : GENERAL ou DATABASE
Si doute → DATABASE
""";
}