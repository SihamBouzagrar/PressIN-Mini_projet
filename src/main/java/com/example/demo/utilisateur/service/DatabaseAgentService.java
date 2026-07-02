package com.example.demo.utilisateur.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.utilisateur.entity.*;
import com.example.demo.utilisateur.repository.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseAgentService {
    
    private final CommandeRepository commandeRepository;
    private final LivraisonRepository livraisonRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final CommandeService commandeService;
    private final FuzzyMatcher fuzzyMatcher; // Injection du matcher flou

    public String handle(String message, Long clientId) {
      if (clientId == null) {
            return """
┌─────────────────────────────────────────┐
│  👋  BIENVENUE !                        │
├─────────────────────────────────────────┤
│                                         │
│  Pour accéder à vos informations        │
│  personnelles, veuillez vous connecter   │
│  à votre compte.                        │
│                                         │
└─────────────────────────────────────────┘""";
        }
        if (message == null || message.isBlank()) {
            return "❓ Je n'ai pas compris. Comment puis-je vous aider ?";
        }
        // Utilisation du matching flou au lieu des regex strictes
  // Remplacez TOUTES les occurrences de matchesCategory par matchesDbCategory

if (fuzzyMatcher.matchesDbCategory(message, "ARTICLES")) {
    return getArticles(clientId);
}
if (fuzzyMatcher.matchesDbCategory(message, "DERNIERE") || 
    (fuzzyMatcher.matchesDbCategory(message, "COMMANDES") && 
     fuzzyMatcher.matchesDbCategory(message, "DERNIERE"))) {
    return getLastCommande(clientId);
}
if (fuzzyMatcher.matchesDbCategory(message, "COMMANDES")) {
    return getCommandes(clientId);
}
if (fuzzyMatcher.matchesDbCategory(message, "STATUT")) {
    return getStatuts(clientId);
}
if (fuzzyMatcher.matchesDbCategory(message, "PRIX")) {
    return getMontant(clientId);
}
if (fuzzyMatcher.matchesDbCategory(message, "LIVRAISON")) {
    return getLivraisons(clientId);
}
if (fuzzyMatcher.matchesDbCategory(message, "SERVICES")) {
    return getServices();
}
if (fuzzyMatcher.matchesDbCategory(message, "PROFIL")) {
    return getUser(clientId);
}

        // Message d'aide contextuel et personnalisé
        return getContextualHelp(message);
    }

    // ============ MÉTHODES DE DONNÉES (inchangées) ============
    
    private String getUser(Long clientId) {
        Users user = userRepository.findById(clientId).orElse(null);
        if (user == null) return "Utilisateur introuvable.";
        
      return String.format("""
┌─────────────────────────────────────────┐
│  👤  VOTRE PROFIL                       │
├─────────────────────────────────────────┤
│                                         │
│  📝  Nom complet    %s %s               │
│  📧  Email          %s                  │
│  📱  Téléphone      %s                  │
│  🆔  CIN            %s                  │
│                                         │
└─────────────────────────────────────────┘""", 
            user.getFirstname(), user.getLastname(),
            user.getEmail(), user.getTelephone(), user.getCin());
    }

    private String getLastCommande(Long clientId) {
        List<Commande> list = commandeRepository.findByClientIdOrderByIdDesc(clientId);
        if (list.isEmpty()) return "📭 Vous n'avez aucune commande pour le moment.";
        
        Commande c = list.get(0);
        return String.format("""
            📦 **Dernière commande**
            
            N° commande : #%d
            📊 Statut : %s
            💰 Total : %.2f MAD
            """, c.getId(), c.getStatut(), c.getMontantFinal());
    }

    private String getArticles(Long clientId) {
        List<Commande> commandes = commandeService.findByClientId(clientId);
        if (commandes.isEmpty()) return "📭 Vous n'avez aucune commande.";

        StringBuilder sb = new StringBuilder("📦 **Articles déposés**\n\n");
        boolean hasArticles = false;
        
        for (Commande c : commandes) {
            if (c.getArticles() == null || c.getArticles().isEmpty()) continue;
            hasArticles = true;
            
            sb.append("Commande #").append(c.getId()).append(" :\n");
            for (Article a : c.getArticles()) {
                String nom = a.getNom() != null ? a.getNom() : "Article non spécifié";
                sb.append(String.format("%s Commande #%d → %s\n", getStatusEmoji(c.getStatut().name()), c.getId(), c.getStatut()));
                sb.append("\n");
            }
            sb.append("\n");
        }
        
        if (!hasArticles) return "📭 Aucun article trouvé dans vos commandes.";
        
        sb.append("💡 Souhaitez-vous le statut de vos commandes ou un récapitulatif ?");
        return sb.toString();
    }

    private String getCommandes(Long clientId) {
        List<Commande> list = commandeRepository.findByClientId(clientId);
        if (list.isEmpty()) return "📭 Vous n'avez aucune commande.";

        StringBuilder sb = new StringBuilder("📦 **Vos commandes**\n\n");
        for (Commande c : list) {
            sb.append(String.format("• Commande #%d | %s | %.2f MAD\n", 
                c.getId(), c.getStatut(), c.getMontantTotal()));
        }
        return sb.toString();
    }

    private String getMontant(Long clientId) {
        List<Commande> list = commandeRepository.findByClientId(clientId);
        double total = list.stream()
            .mapToDouble(c -> c.getMontantFinal() != null ? c.getMontantFinal() : 0)
            .sum();
        
        long unpaidCount = list.stream()
            .filter(c -> c.getStatut() != null && !c.getStatut().name().toLowerCase().contains("PAYE"))
            .count();
        
        if (total == 0) return "✅ Vous n'avez aucun montant à payer.";
        
        return String.format("""
            💰 **Montant à payer**
            
            Total : %.2f MAD
            Commandes en attente : %d
            """, total, unpaidCount);
    }

    private String getStatuts(Long clientId) {
        List<Commande> list = commandeRepository.findByClientId(clientId);
        if (list.isEmpty()) return "📭 Aucune commande trouvée.";

        StringBuilder sb = new StringBuilder("📊 **Statuts de vos commandes**\n\n");
        for (Commande c : list) {
           String emoji = getStatusEmoji(c.getStatut().name());
            sb.append(String.format("%s Commande #%d → %s\n", emoji, c.getId(), c.getStatut()));
        } 
        return sb.toString();
    }

    private String getLivraisons(Long clientId) {
        List<Livraison> list = livraisonRepository.findByCommande_Client_Id(clientId);
        if (list.isEmpty()) return "🚚 Aucune livraison trouvée.";

        StringBuilder sb = new StringBuilder("🚚 **Vos livraisons**\n");
        for (Livraison l : list) {
            sb.append(String.format("\n📦 Livraison #%d\n", l.getId()));
            
            if (l.getAdresseCollecte() != null) {
                sb.append(String.format("📍 Collecte : %s, %s\n", 
                    l.getAdresseCollecte().getRue(), l.getAdresseCollecte().getVille()));
            }
            if (l.getAdresseLivraison() != null) {
                sb.append(String.format("🏠 Destination : %s, %s\n", 
                    l.getAdresseLivraison().getRue(), l.getAdresseLivraison().getVille()));
            }
            sb.append(String.format("📊 Statut : %s\n", l.getStatut()));
            if (l.getDateLivraisonPrevue() != null) {
                sb.append(String.format("📅 Prévue le : %s\n", l.getDateLivraisonPrevue()));
            }
        }
        return sb.toString();
    }

    private String getServices() {
        List<ServicePressing> list = serviceRepository.findAll();
        if (list.isEmpty()) return "🛠 Aucun service disponible pour le moment.";

        StringBuilder sb = new StringBuilder("🛠 **Nos services disponibles**\n\n");
        for (ServicePressing s : list) {
            sb.append(String.format("• %s | %s | %.2f MAD\n", 
                s.getNom(), s.getCategorie(), s.getPrixBase()));
        }
        return sb.toString();
    }

    // ============ MÉTHODES UTILITAIRES ============

    private String getStatusEmoji(String statut) {
        if (statut == null) return "⚪";
        String s = statut.toLowerCase();
        if (s.contains("livré") || s.contains("termine")) return "✅";
        if (s.contains("cours") || s.contains("traitement")) return "🔄";
        if (s.contains("attente") || s.contains("preparation")) return "⏳";
        if (s.contains("annul")) return "❌";
        return "📦";
    }

    /**
     * Message d'aide contextuel qui analyse le message pour proposer des suggestions
     */
    private String getContextualHelp(String message) {
        String normalized = message.toLowerCase();
        StringBuilder suggestions = new StringBuilder();
        
        // Détecter des mots partiels pour suggérer
        if (normalized.contains("bonjour") || normalized.contains("salut") || normalized.contains("hey")) {
            return "👋 Bonjour ! Je suis votre assistant PressIN. Je peux vous aider avec :\n\n"
                 + "📦 Vos commandes et articles\n"
                 + "🚚 Vos livraisons\n"
                 + "💰 Vos montants à payer\n"
                 + "🛠 Nos services\n"
                 + "👤 Votre profil\n\n"
                 + "Que souhaitez-vous savoir ?";
        }
        
        if (normalized.contains("merci") || normalized.contains("thanks")) {
            return "🙏 Je vous en prie ! N'hésitez pas si vous avez d'autres questions.";
        }
        
        if (normalized.contains("aide") || normalized.contains("help")) {
            return getHelp();
        }

        // Message par défaut intelligent
        return """
┌─────────────────────────────────────────┐
│  🤔  JE N'AI PAS COMPRIS                │
├─────────────────────────────────────────┤
│                                         │
│  Essayez avec des mots comme :          │
│                                         │
│  • "mes commandes"                      │
│  • "statut"                             │
│  • "combien payer"                      │
│  • "livraison"                          │
│  • "services"                           │
│  • "mon profil"                         │
│                                         │
│  💡 Même avec des fautes, ça marche !   │
│                                         │
└─────────────────────────────────────────┘""";
    }

    private String getHelp() {
        return """
            👋 **Je suis votre assistant PressIN**
            
            Voici tout ce que je peux faire :
            
            📦 **Commandes**
               • "mes commandes" - Liste complète
               • "dernière commande" - La plus récente
               • "articles déposés" - Vos vêtements
            
            📊 **Suivi**
               • "statut" ou "où en est ma commande"
            
            💰 **Paiement**
               • "combien je dois payer"
               • "montant total"
            
            🚚 **Livraison**
               • "livraison" ou "quand arrive ma commande"
               • "collecte" ou "retrait"
            
            🛠 **Services**
               • "services" ou "tarifs"
            
            👤 **Profil**
               • "mon compte" ou "mes infos"
            """;
    }
}