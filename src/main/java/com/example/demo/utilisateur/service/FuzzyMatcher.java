package com.example.demo.utilisateur.service;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class FuzzyMatcher {

    private static final double SIMILARITY_THRESHOLD = 0.7;

    // ============ KEYWORDS DATABASE ============
    private static final Map<String, List<String>> DB_KEYWORDS = Map.ofEntries(
            Map.entry("ARTICLES",
                    List.of("article", "articles", "depose", "deposé", "produits", "vetement", "vêtement", "habit",
                            "habits", "costume", "robe", "chemise", "pantalon", "laissé", "laisse", "confié")),
            Map.entry("COMMANDES",
                    List.of("commande", "commandes", "cmd", "achat", "historique", "liste", "passe", "passé")),
            Map.entry("DERNIERE", List.of("dernière", "derniere", "dernier", "last", "recent", "récent")),
            Map.entry("STATUT",
                    List.of("statut", "status", "suivi", "etat", "état", "progression", "avancement", "ou en est",
                            "où est", "encours", "en cours", "termine", "terminé")),
            Map.entry("PRIX",
                    List.of("prix", "payer", "paye", "payé", "montant", "total", "cout", "coût", "tarif", "facture",
                            "combien", "solde", "devoir", "dois", "argent", "mad", "dh")),
            Map.entry("LIVRAISON",
                    List.of("livraison", "livré", "livre", "collecte", "planning", "date", "quand", "heure", "retrait",
                            "recuperer", "récupérer", "domicile", "adresse", "transport")),
            Map.entry("SERVICES",
                    List.of("service", "services", "prestation", "lavage", "repassage", "nettoyage", "detachage",
                            "détachage", "pressing", "offre", "offres")),
            Map.entry("PROFIL", List.of("profil", "compte", "infos", "coordonnées", "nom", "prenom", "prénom",
                    "telephone", "email", "cin", "identite")));

    // ============ KEYWORDS GENERAL ============
    private static final Map<String, List<String>> GENERAL_KEYWORDS = Map.ofEntries(
            Map.entry("HORAIRE",
                    List.of("horaire", "horaires", "ouverture", "ouvert", "ferme", "fermé", "fermeture", "heure",
                            "heures", "matin", "apres midi", "soir", "dimanche", "samedi", "jours", "journée")),
            Map.entry("TARIF",
                    List.of("tarif", "tarifs", "prix", "combien coute", "coûte", "cout", "tarification", "gratuit",
                            "cher", "pas cher", "reduction", "promo", "offre")),
            Map.entry("SERVICE_INFO",
                    List.of("service", "services", "prestation", "prestations", "lavage", "repassage", "nettoyage",
                            "sec", "detache", "détache", "tache", "taches", "plis", "froissé", "odeur", "desinfecte")),
            Map.entry("DELAI",
                    List.of("delai", "délai", "duree", "durée", "temps", "rapid", "rapide", "express", "urgent",
                            "quand pret", "prêt", "jours", "heures", "24h", "48h")),
            Map.entry("LIVRAISON_INFO",
                    List.of("livraison", "livrer", "collecte", "domicile", "adresse", "zone", "secteur", "ville",
                            "région", "disponible", "transport", "retrait", "magasin")),
            Map.entry("QUALITE",
                    List.of("qualite", "qualité", "propre", "propreté", "proprete", "soin", "douceur", "tissu",
                            "tissus", "délicat", "delicat", "sensible", "silk", "laine", "cuir")),
            Map.entry("FONCTIONNEMENT",
                    List.of("fonctionne", "marche", "comment", "comment ca marche", "utiliser", "application", "app",
                            "site", "inscription", "compte", "creer", "créer", "connecter")),
            Map.entry("CONTACT",
                    List.of("contact", "telephone", "téléphone", "appeler", "email", "mail", "adresse", "localisation",
                            "ou etes", "où êtes", "situe", "situé", "bureau")),
            Map.entry("RECLAMATION",
                    List.of("reclamation", "réclamation", "plainte", "probleme", "problème", "insatisfait", "erreur",
                            "retour", "remboursement", "garantie", "dommage", "perdu")),
            Map.entry("BONJOUR",
                    List.of("bonjour", "salut", "hey", "hello", "hi", "coucou", "wesh", "bonsoir", "bonjourr", "salu",
                            "slt")),
            Map.entry("MERCI",
                    List.of("merci", "thanks", "thank", "thx", "mercii", "ok", "super", "genial", "génial", "parfait",
                            "cool")),
            Map.entry("AU_REVOIR", List.of("au revoir", "bye", "ciao", "a plus", "à plus", "bonne journée",
                    "bonne soiree", "a bientot", "à bientôt")));

    // ============ MÉTHODES PUBLIQUES ============

    public boolean matchesDbCategory(String message, String category) {
        return matches(message, DB_KEYWORDS.getOrDefault(category, List.of()));
    }

    public boolean matchesGeneralCategory(String message, String category) {
        return matches(message, GENERAL_KEYWORDS.getOrDefault(category, List.of()));
    }

    public Optional<String> findBestDbCategory(String message) {
        return findBest(message, DB_KEYWORDS);
    }

    public Optional<String> findBestGeneralCategory(String message) {
        return findBest(message, GENERAL_KEYWORDS);
    }

    public boolean isGreeting(String message) {
        return matchesGeneralCategory(message, "BONJOUR");
    }

    public boolean isThanks(String message) {
        return matchesGeneralCategory(message, "MERCI");
    }

    public boolean isGoodbye(String message) {
        return matchesGeneralCategory(message, "AU_REVOIR");
    }

    // ============ MÉTHODES PRIVÉES ============

    private boolean matches(String message, List<String> keywords) {
        if (message == null || message.isBlank())
            return false;
        String normalizedMsg = normalize(message);

        for (String keyword : keywords) {
            String normKw = normalize(keyword);
            if (normalizedMsg.contains(normKw))
                return true;

            for (String word : normalizedMsg.split("\\s+")) {
                if (word.length() >= 3 && calculateSimilarity(word, normKw) >= SIMILARITY_THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }

    private Optional<String> findBest(String message, Map<String, List<String>> categories) {
        String best = null;
        double bestScore = 0.0;
        String normalizedMsg = normalize(message);

        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
            double score = calculateCategoryScore(normalizedMsg, entry.getValue());
            if (score > bestScore && score >= SIMILARITY_THRESHOLD) {
                bestScore = score;
                best = entry.getKey();
            }
        }
        return Optional.ofNullable(best);
    }

    private double calculateCategoryScore(String message, List<String> keywords) {
        double max = 0.0;
        String[] words = message.split("\\s+");

        for (String kw : keywords) {
            String normKw = normalize(kw);
            if (message.contains(normKw))
                return 1.0;
            for (String word : words) {
                if (word.length() >= 3) {
                    max = Math.max(max, calculateSimilarity(word, normKw));
                }
            }
        }
        return max;
    }

    String normalize(String text) {
        if (text == null)
            return "";
        return text.toLowerCase()
                .replaceAll("[éèêë]", "e")
                .replaceAll("[àâä]", "a")
                .replaceAll("[ùûü]", "u")
                .replaceAll("[ôö]", "o")
                .replaceAll("[îï]", "i")
                .replaceAll("[ç]", "c")
                .replaceAll("[^a-z0-9\\s]", "")
                .trim();
    }

    private double calculateSimilarity(String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0)
            return 1.0;
        return 1.0 - ((double) levenshteinDistance(s1, s2) / maxLen);
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++)
            dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++)
            dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }
}