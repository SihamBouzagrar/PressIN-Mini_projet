package com.example.demo.rest;

import com.example.demo.utilisateur.service.*;
import com.example.demo.dto.ChatRequest;
import com.example.demo.dto.ChatResponse;
import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    private final RouterAgentService routerAgentService;
    private final DatabaseAgentService databaseAgentService;
    private final GeneralAgentService generalAgentService;
    private final UserRepository userRepository;
    private final FuzzyMatcher fuzzyMatcher;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String message = request.getMessage();
        Long clientId = request.getClientId();

        if (isWelcomeTrigger(message)) {
            return ResponseEntity.ok(buildWelcomeResponse(clientId));
        }

        String socialResponse = handleSocialMessage(message);
        if (socialResponse != null) {
            return ResponseEntity.ok(new ChatResponse(socialResponse, "SOCIAL"));
        }

        String intent = routerAgentService.detectIntent(message);
        String response;

        if ("DATABASE".equals(intent)) {
            response = databaseAgentService.handle(message, clientId);
        } else {
            response = generalAgentService.askGeneral(message);
        }

        return ResponseEntity.ok(new ChatResponse(response, intent));
    }

    @GetMapping("/welcome/{clientId}")
    public ResponseEntity<ChatResponse> welcome(@PathVariable Long clientId) {
        return ResponseEntity.ok(buildWelcomeResponse(clientId));
    }

    // ============ MÉTHODES PRIVÉES ============

    private boolean isWelcomeTrigger(String message) {
        if (message == null) return true;
        String lower = message.toLowerCase().trim();
        return lower.isEmpty() 
            || lower.matches("(bonjour|salut|hey|hello|hi|coucou|wesh|bonsoir).*")
            || lower.equals("start") 
            || lower.equals("debut") 
            || lower.equals("début")
            || lower.equals("ouvrir")
            || lower.equals("lancer");
    }

    private String handleSocialMessage(String message) {
        if (fuzzyMatcher.isThanks(message)) {
            return "🙏 Avec plaisir ! N'hésitez pas si vous avez besoin d'autre chose.";
        }
        if (fuzzyMatcher.isGoodbye(message)) {
            return "👋 Au revoir ! Merci de votre confiance. À bientôt chez PressIN !";
        }
        return null;
    }

    private ChatResponse buildWelcomeResponse(Long clientId) {
        String userName = getUserFirstName(clientId);
        String greeting = getTimeBasedGreeting();
        String timeEmoji = getTimeEmoji();
        
        // Construction sans String.format pour éviter les erreurs de %
       String header = timeEmoji + "  " + greeting + (userName.isEmpty() ? " !" : " " + userName + " !");

    String message =
        header + " Je suis l'assistant virtuel PressIN.\n\n" +
        "Voici comment je peux vous aider :\n\n" +
        "📦 Commandes — voir vos commandes, vos articles déposés et le statut en temps réel\n" +
        "💰 Paiement — montant à payer et historique des factures\n" +
        "🚚 Livraison — suivi de livraison et créneaux de collecte / retrait\n" +
        "🛠 Services — lavage, repassage, nettoyage à sec, détachage, tarifs et délais\n" +
        "👤 Profil — vos informations personnelles\n\n" +
        "💡 Astuce : écrivez naturellement, même avec des fautes de frappe — je vous comprendrai.\n\n" +
        "Que souhaitez-vous faire aujourd'hui ?";

    return new ChatResponse(message, "WELCOME");
}

    private String padRight(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }

    private String getUserFirstName(Long clientId) {
        if (clientId == null) return "";
        return userRepository.findById(clientId)
            .map(Users::getFirstname)
            .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase())
            .orElse("");
    }

    private String getTimeBasedGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 5) return "Bonsoir";
        if (hour < 12) return "Bonjour";
        if (hour < 18) return "Bon après-midi";
        return "Bonsoir";
    }

    private String getTimeEmoji() {
        int hour = LocalTime.now().getHour();
        if (hour < 6) return "🌙";
        if (hour < 12) return "☀️";
        if (hour < 18) return "🌤️";
        return "🌙";
    }
}
