package com.example.demo.utilisateur.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeneralAgentService {

        private final WebClient webClient;
        private final String model;

        public GeneralAgentService(WebClient.Builder builder,
                        @Value("${groq.api.key}") String apiKey,
                        @Value("${groq.api.url}") String groqUrl,
                        @Value("${groq.model}") String model) {

                this.model = model;

                this.webClient = builder
                                .baseUrl(groqUrl)
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                                .build();
        }

      private static final String SYSTEM_PROMPT = """
Tu es un assistant intelligent de l'application PressIN (pressing textile).

🎯 RÔLE :
Aider uniquement sur :
SERVICES DISPONIBLES :
- lavage
- repassage
- nettoyage à sec
- détachage
- livraison à domicile ou en magasin

- lavage
- repassage
- nettoyage à sec
- détachage
- suivi de commande
- livraison
-Type de livraison possible 
🚚 COMMANDE :
Une commande se fait dans l'application en sélectionnant des articles et un type de livraison.


RÈGLES :
- Réponse courte et simple
- Ne jamais inventer de services supplémentaires
- Toujours répondre directement à la question
- Si hors sujet → dire :
"Je peux vous aider uniquement avec les services PressIN (pressing et livraison)."

🚫 INTERDICTIONS STRICTES :
Ne parle JAMAIS de :
- paiement ou carte bancaire
- email de confirmation
- processus e-commerce
- sites externes


📌 RÈGLE IMPORTANTE :
- Ne donne pas de procédure inventée.
- Réponds de manière courte, simple et adaptée à PressIN.
- Si la question est hors périmètre → répondre :
  "Je peux vous aider uniquement avec les services PressIN (pressing, commandes, livraison)."
""";
private final ObjectMapper objectMapper = new ObjectMapper();

public String askGeneral(String userMessage) {

    try {
        Map<String, Object> body = new HashMap<>();

        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userMessage)
        ));
        body.put("temperature", 0.7);
        body.put("max_completion_tokens", 500);
        body.put("stream", false);

        String response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("GROQ RAW = " + response);

        return extractContent(response); // OK

    } catch (Exception e) {
        System.out.println("GROQ ERROR = " + e.getMessage());
        return "Erreur IA (service indisponible)";
    }
}
     private String extractContent(String groqResponse) {
    try {
        JsonNode root = objectMapper.readTree(groqResponse);

        JsonNode content = root
                .path("choices")
                .path(0)
                .path("message")
                .path("content");

        if (content.isMissingNode() || content.asText().isBlank()) {
            return "Service IA indisponible.";
        }

        return content.asText();

    } catch (Exception e) {
        System.out.println("PARSE ERROR = " + groqResponse);
        return "Erreur IA (format invalide)";
    }
}
}
