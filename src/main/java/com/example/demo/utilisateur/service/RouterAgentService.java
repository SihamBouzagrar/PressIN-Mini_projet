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

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String model;

  private static final String SYSTEM_PROMPT = """
Tu es un routeur intelligent pour l'application PressIN.

Tu dois classer chaque message en UNE SEULE catégorie :

=========================
DATABASE
=========================
Utilise DATABASE si la question concerne :
- prix
- montant à payer
- paiement

- total
- remise
- commande utilisateur
- articles déposés
- vêtements déposés
- habits déposés
- contenu de commande
- articles commandés
- statut
- livraison
- collecte


Exemples :
- Combien vais-je payer ?
- Quel est le montant total ?
- Ai-je une remise ?
Exemples :
- Quels articles ai-je déposés ?
- Qu'est-ce que j'ai laissé au pressing ?
- Mes vêtements ?
- Ai-je déposé un costume ?

- Statut commande

=========================
GENERAL
=========================
Utilise GENERAL si la question concerne :
- services de pressing
- lavage, repassage, nettoyage
- fonctionnement de l'application
- conseils textiles

=========================
RÈGLE ABSOLUE
=========================
Si doute → choisir DATABASE

Réponds uniquement : GENERAL ou DATABASE
""";

    public String detectIntent(String userMessage) {

        if (userMessage == null || userMessage.isBlank()) {
            return "GENERAL";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            Map<String, Object> body = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", userMessage)),
                    "temperature", 0,
                    "max_completion_tokens", 10,
                    "stream", false);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            String response = restTemplate.postForObject(
                    groqUrl + "/chat/completions",
                    request,
                    String.class);

            return extractIntent(response);

        } catch (Exception e) {
            return "GENERAL"; // fallback safe
        }
    }

    private String extractIntent(String groqResponse) {

        try {
            JsonNode root = objectMapper.readTree(groqResponse);

            JsonNode choices = root.path("choices");

            if (!choices.isArray() || choices.isEmpty()) {
                return "GENERAL";
            }

            JsonNode messageNode = choices.get(0)
                    .path("message")
                    .path("content");

            String content = messageNode.asText("")
                    .trim()
                    .toUpperCase();

            if ("DATABASE".equals(content)) {
                return "DATABASE";
            }

            if ("GENERAL".equals(content)) {
                return "GENERAL";
            }

            // fallback si réponse bizarre
            if (content.contains("DATABASE"))
                return "DATABASE";

            return "GENERAL";

        } catch (Exception e) {
            return "GENERAL";
        }
    }
}