package com.example.demo.utilisateur.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeneralAgentService {

    private final WebClient webClient;
    private final String model;
    private final FuzzyMatcher fuzzyMatcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<String, String> LOCAL_RESPONSES = new HashMap<>();

 static {
    LOCAL_RESPONSES.put("HORAIRE", """
            🕐 Horaires d'ouverture

            Lundi – Vendredi : 08h00 – 20h00
            Samedi : 09h00 – 18h00
            Dimanche : fermé

            💡 Astuce : passez votre commande avant 10h en service Express pour une livraison le jour même !""");

    LOCAL_RESPONSES.put("TARIF", """
            💰 Nos tarifsLavage standard : à partir de 25 MAD
            Repassage : 15 MAD / pièce
            Nettoyage à sec : 35 MAD / pièce
            Détachage : 20 MAD / zone
            Livraison à domicile : 30 MAD (gratuite dès 150 MAD)

            📱 Les tarifs exacts s'affichent dans l'app selon les articles sélectionnés.""");

    LOCAL_RESPONSES.put("SERVICE_INFO", """
            🛠 Nos services

            ✨ Lavage — tous textiles
            ✨ Repassage — vapeur haute pression
            ✨ Nettoyage à sec — textiles délicats
            ✨ Détachage — taches tenaces
            ✨ Désinfection — traitement antibactérien
            ✨ Livraison — à domicile ou en magasin

            🌿 Tous nos produits sont 100% écologiques.""");

    LOCAL_RESPONSES.put("DELAI", """
            ⏱️ Délais de traitement

            Standard : 24 à 48h
            Express : 4 à 6h (+50%)
            Nettoyage à sec : 48 à 72h
            Détachage spécial : +24h

            📅 La date exacte vous est communiquée à la commande et visible dans le suivi.""");

    LOCAL_RESPONSES.put("LIVRAISON_INFO", """
            🚚 Livraison & collecte

            📍 Zones desservies : Casablanca, Rabat, Marrakech

            🏠 À domicile : 7j/7, sur créneaux
            🏪 En magasin : retrait gratuit

            ⏰ Créneaux disponibles : 9h–12h ou 14h–18h

            ✅ Livraison gratuite dès 150 MAD de commande.""");

    LOCAL_RESPONSES.put("QUALITE", """
            ✨ Notre engagement qualité

            • Produits écologiques certifiés
            • Machines professionnelles
            • Personnel formé aux textiles délicats
            • Contrôle qualité à chaque étape
            • Garantie satisfaction

            🧴 Détergents hypoallergéniques, sans parfum agressif.""");

    LOCAL_RESPONSES.put("FONCTIONNEMENT", """
            📱 Comment utiliser PressIN

            1️⃣ Créez votre compte
            2️⃣ Sélectionnez vos articles et services
            3️⃣ Choisissez livraison ou retrait en magasin
            4️⃣ Suivez votre commande en direct
            5️⃣ Payez en ligne ou à la livraison

            💡 Besoin d'aide à n'importe quelle étape ? Je suis là pour vous guider !""");

    LOCAL_RESPONSES.put("CONTACT", """
            📞 Nous contacter

            💬 Chat (ici même) : disponible 7j/7
            📧 support@pressin.ma
            ☎️ 0522-XX-XX-XX
            📍 123 Bd Mohammed V, Casablanca

            ⏰ Du lundi au samedi, 8h–20h""");

    LOCAL_RESPONSES.put("RECLAMATION", """
            😔 Réclamation

            Désolé pour ce désagrément.

            Pour un traitement rapide :
            1️⃣ Décrivez le problème
            2️⃣ Précisez le numéro de commande
            3️⃣ Un responsable vous recontacte sous 24h

            📧 reclamation@pressin.ma

            Votre satisfaction est notre priorité.""");

    LOCAL_RESPONSES.put("BONJOUR", """
            👋 Bienvenue chez PressIN !

            Je peux vous renseigner sur :

            🛠 Services & tarifs
            ⏰ Horaires & délais
            🚚 Livraison & zones desservies
            📱 Fonctionnement de l'application

            Posez-moi votre question, je suis là pour vous aider !""");

    LOCAL_RESPONSES.put("MERCI", """
            🙏 Avec plaisir !

            N'hésitez pas si vous avez une autre question.

            Bonne journée 😊""");

    LOCAL_RESPONSES.put("AU_REVOIR", """
            👋 Au revoir et à bientôt !

            Merci de votre confiance.

            🎁 Profitez du code BIENVENUE pour la livraison gratuite sur votre première commande.""");
}
    

    public GeneralAgentService(WebClient.Builder builder,
            @Value("${groq.api.key}") String apiKey,
            @Value("${groq.api.url}") String groqUrl,
            @Value("${groq.model}") String model,
            FuzzyMatcher fuzzyMatcher) {
        this.model = model;
        this.fuzzyMatcher = fuzzyMatcher;
        this.webClient = builder
            .baseUrl(groqUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .build();
    }

    public String askGeneral(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return LOCAL_RESPONSES.get("BONJOUR");
        }

        String localResponse = findLocalResponse(userMessage);
        if (localResponse != null) {
            return localResponse;
        }

        return askGroq(userMessage);
    }

    private String findLocalResponse(String message) {
        if (fuzzyMatcher.matchesGeneralCategory(message, "BONJOUR")) return LOCAL_RESPONSES.get("BONJOUR");
        if (fuzzyMatcher.matchesGeneralCategory(message, "MERCI")) return LOCAL_RESPONSES.get("MERCI");
        if (fuzzyMatcher.matchesGeneralCategory(message, "AU_REVOIR")) return LOCAL_RESPONSES.get("AU_REVOIR");
        if (fuzzyMatcher.matchesGeneralCategory(message, "HORAIRE")) return LOCAL_RESPONSES.get("HORAIRE");
        if (fuzzyMatcher.matchesGeneralCategory(message, "TARIF")) return LOCAL_RESPONSES.get("TARIF");
        if (fuzzyMatcher.matchesGeneralCategory(message, "SERVICE_INFO")) return LOCAL_RESPONSES.get("SERVICE_INFO");
        if (fuzzyMatcher.matchesGeneralCategory(message, "DELAI")) return LOCAL_RESPONSES.get("DELAI");
        if (fuzzyMatcher.matchesGeneralCategory(message, "LIVRAISON_INFO")) return LOCAL_RESPONSES.get("LIVRAISON_INFO");
        if (fuzzyMatcher.matchesGeneralCategory(message, "QUALITE")) return LOCAL_RESPONSES.get("QUALITE");
        if (fuzzyMatcher.matchesGeneralCategory(message, "FONCTIONNEMENT")) return LOCAL_RESPONSES.get("FONCTIONNEMENT");
        if (fuzzyMatcher.matchesGeneralCategory(message, "CONTACT")) return LOCAL_RESPONSES.get("CONTACT");
        if (fuzzyMatcher.matchesGeneralCategory(message, "RECLAMATION")) return LOCAL_RESPONSES.get("RECLAMATION");
        return null;
    }

    private String askGroq(String userMessage) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("model", model);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
            messages.add(Map.of("role", "user", "content", userMessage));
            body.put("messages", messages);

            body.put("temperature", 0.7);
            body.put("max_completion_tokens", 500);
            body.put("stream", false);

            String response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            String result = extractContent(response);
            
            // Si Groq retourne une erreur, on retourne une réponse locale par défaut
            if (result.startsWith("❌") || result.startsWith("⚠️") || result.startsWith("Erreur") || result.startsWith("Service")) {
                return getFallbackResponse(userMessage);
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("GROQ ERROR = " + e.getMessage());
            return getFallbackResponse(userMessage);
        }
    }

    private String getFallbackResponse(String userMessage) {
        // Fallback intelligent selon le contexte
        String lower = userMessage.toLowerCase();
        if (lower.contains("service") || lower.contains("lavage") || lower.contains("repassage") || lower.contains("nettoyage")) {
            return LOCAL_RESPONSES.get("SERVICE_INFO");
        }
        if (lower.contains("prix") || lower.contains("tarif") || lower.contains("combien") || lower.contains("coute")) {
            return LOCAL_RESPONSES.get("TARIF");
        }
        if (lower.contains("heure") || lower.contains("ouvert") || lower.contains("ferme")) {
            return LOCAL_RESPONSES.get("HORAIRE");
        }
        if (lower.contains("livraison") || lower.contains("livrer") || lower.contains("collecte")) {
            return LOCAL_RESPONSES.get("LIVRAISON_INFO");
        }
        if (lower.contains("delai") || lower.contains("duree") || lower.contains("temps") || lower.contains("rapide")) {
            return LOCAL_RESPONSES.get("DELAI");
        }
        if (lower.contains("qualite") || lower.contains("propre") || lower.contains("produit")) {
            return LOCAL_RESPONSES.get("QUALITE");
        }
        if (lower.contains("comment") || lower.contains("marche") || lower.contains("utiliser")) {
            return LOCAL_RESPONSES.get("FONCTIONNEMENT");
        }
        if (lower.contains("contact") || lower.contains("telephone") || lower.contains("email") || lower.contains("adresse")) {
            return LOCAL_RESPONSES.get("CONTACT");
        }
        if (lower.contains("probleme") || lower.contains("reclamation") || lower.contains("plainte")) {
            return LOCAL_RESPONSES.get("RECLAMATION");
        }
        
        return """
┌─────────────────────────────────────────┐
│  🤔  JE N'AI PAS TROUVÉ DE RÉPONSE      │
├─────────────────────────────────────────┤
│                                         │
│  Voici ce que je peux faire :           │
│                                         │
│  • 🛠  Services & tarifs                │
│  • ⏰  Horaires & délais                │
│  • 🚚  Livraison & zones                │
│  • 📱  Fonctionnement de l'app          │
│                                         │
│  Posez-moi votre question autrement !   │
│                                         │
└─────────────────────────────────────────┘""";
    }

    private String extractContent(String groqResponse) {
        try {
            if (groqResponse == null || groqResponse.isBlank()) {
                return "❌ Service IA indisponible (réponse vide)";
            }
            
            JsonNode root = objectMapper.readTree(groqResponse);
            
            // Vérifier si c'est une erreur API
            if (root.has("error")) {
                String errorMsg = root.path("error").path("message").asText("Erreur inconnue");
                System.out.println("GROQ API ERROR = " + errorMsg);
                return "❌ Erreur IA : " + errorMsg;
            }
            
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.asText().isBlank()) {
                return "❌ Service IA indisponible (contenu vide)";
            }
            return content.asText();
        } catch (Exception e) {
            System.out.println("PARSE ERROR = " + groqResponse);
            return "❌ Erreur IA (format invalide)";
        }
    }

    private static final String SYSTEM_PROMPT = """
Tu es un assistant intelligent de l'application PressIN (pressing textile).
🎯 RÔLE : Aider uniquement sur les services PressIN
SERVICES : Lavage, repassage, nettoyage à sec, détachage, livraison, suivi
RÈGLES : Réponse courte (max 3 phrases), ne jamais inventer, répondre directement
Si hors sujet → "Je peux vous aider uniquement avec les services PressIN."
🚫 INTERDICTIONS : Paiement/carte bancaire, sites externes, procédures inventées
""";
}