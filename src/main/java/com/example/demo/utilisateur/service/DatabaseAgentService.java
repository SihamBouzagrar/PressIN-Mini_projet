package com.example.demo.utilisateur.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.utilisateur.entity.Article;
import com.example.demo.utilisateur.entity.Commande;
import com.example.demo.utilisateur.entity.Livraison;
import com.example.demo.utilisateur.entity.ServicePressing;
import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.CommandeRepository;
import com.example.demo.utilisateur.repository.LivraisonRepository;
import com.example.demo.utilisateur.repository.ServiceRepository;
import com.example.demo.utilisateur.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class DatabaseAgentService {

    private final CommandeRepository commandeRepository;
    private final LivraisonRepository livraisonRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final CommandeService commandeService;

    public String handle(String message, Long clientId) {

        if (clientId == null) return "Client non identifié.";
        if (message == null || message.isBlank()) return "Message vide.";

        message = message.toLowerCase();
        // ARTICLES (priorité haute)
if (message.matches(".*(article|articles|déposé|produits).*")) {
    return getArticles(clientId);
}

        // ================= COMMANDES =================
        if (message.contains("commande")) return getCommandes(clientId);
        if (message.contains("dernière")) return getLastCommande(clientId);
        if (message.matches(".*(statut|suivi).*")) return getStatuts(clientId);
        if (message.matches(".*(prix|payer|montant).*")) return getMontant(clientId);

        // ================= LIVRAISONS =================
        if (message.matches(".*(livraison|livré|collecte|planning).*")) return getLivraisons(clientId);

        // ================= SERVICES =================
        if (message.matches(".*(service|prestation|type service).*")) return getServices();

     

        return "Je peux vous aider avec : commandes, livraisons, services et profil utilisateur.";
    }
    private String getUser(Long clientId) {

    Users user = userRepository.findById(clientId)
            .orElse(null);

    if (user == null) {
        return "Utilisateur introuvable.";
    }

    return "👤 Profil utilisateur :"
            + "\nNom : " + user.getFirstname() + " " + user.getLastname()
            + "\nEmail : " + user.getEmail()
            + "\nTéléphone : " + user.getTelephone()
        
            + "\nCIN : " + user.getCin();
}
    private String getLastCommande(Long clientId) {



    List<Commande> list = commandeRepository.findByClientIdOrderByIdDesc(clientId);



    if (list.isEmpty()) {

        return "Vous n'avez aucune commande.";

    }



    Commande c = list.get(0);



    return "📦 Dernière commande : " +

            "\nCommande #" + c.getId() +

            " | Statut : " + c.getStatut() +

            " | Total : " + c.getMontantFinal() + " MAD";

}
private String getArticles(Long clientId) {

    List<Commande> commandes = commandeService.findByClientId(clientId);

    if (commandes.isEmpty()) {
        return "Vous n’avez aucune commande.";
    }

    StringBuilder sb = new StringBuilder();

    sb.append("📦 Voici les articles que vous avez déposés :\n\n");

    for (Commande c : commandes) {

        if (c.getArticles() == null || c.getArticles().isEmpty()) {
            continue;
        }

        for (Article a : c.getArticles()) {

            String nom = a.getNom() != null ? a.getNom() : "article";
           

            sb.append("• ")
           
              .append(" ")
              .append(nom)
              .append(" (")
         
              .append(")\n");
        }
    }

    sb.append("\n💡 Souhaitez-vous un récapitulatif ou le statut de vos commandes ?");

    return sb.toString();
}

 private String getCommandes(Long clientId) {



        List<Commande> list = commandeRepository.findByClientId(clientId);



        if (list.isEmpty()) {

            return "Vous n'avez aucune commande.";

        }



        StringBuilder sb = new StringBuilder("📦 Vos commandes : ");



        for (Commande c : list) {

            sb.append("\nCommande #")

              .append(c.getId())

              .append(" | Statut : ")

              .append(c.getStatut())

              .append(" | Total : ")

              .append(c.getMontantTotal())

              .append(" MAD");

        }



        return sb.toString();

    }

private String getMontant(Long clientId) {



    List<Commande> list = commandeRepository.findByClientId(clientId);



    double total = list.stream()

            .mapToDouble(c -> c.getMontantFinal() != null ? c.getMontantFinal() : 0)

            .sum();



    return "💰 Vous devez payer : " + total + " MAD";

}
 private String getStatuts(Long clientId) {



        List<Commande> list = commandeRepository.findByClientId(clientId);



        if (list.isEmpty()) {

            return "Aucune commande trouvée.";

        }



        StringBuilder sb = new StringBuilder("📊 Statuts de vos commandes : ");



        for (Commande c : list) {

            sb.append("\nCommande #")

              .append(c.getId())

              .append(" → ")

              .append(c.getStatut());

        }



        return sb.toString();

    }

private String getLivraisons(Long clientId) {

    List<Livraison> list = livraisonRepository.findByCommande_Client_Id(clientId);
   

    if (list.isEmpty()) {
        return "Aucune livraison trouvée.";
    }

    StringBuilder sb = new StringBuilder("🚚 Vos livraisons :");

    for (Livraison l : list) {

        sb.append("\n\n📦 Livraison #")
          .append(l.getId());

        if (l.getAdresseCollecte() != null) {

            sb.append("\n📍 Collecte : ")
              .append(l.getAdresseCollecte().getRue())
              .append(", ")
              .append(l.getAdresseCollecte().getVille());
        }

        if (l.getAdresseLivraison() != null) {

            sb.append("\n🏠 Destination : ")
              .append(l.getAdresseLivraison().getRue())
              .append(", ")
              .append(l.getAdresseLivraison().getVille());
        }

        sb.append("\n📊 Statut : ")
          .append(l.getStatut());

        sb.append("\n📅 Livraison prévue : ")
          .append(l.getDateLivraisonPrevue());
    }

    return sb.toString();
}

private String getServices() {

    List<ServicePressing> list = serviceRepository.findAll();

    if (list.isEmpty()) {
        return "Aucun service disponible.";
    }

    StringBuilder sb = new StringBuilder("🛠 Services disponibles :");

    for (ServicePressing s : list) {

        sb.append("\n")
          .append(s.getNom())
          .append(" | Catégorie : ")
          .append(s.getCategorie())
          .append(" | Prix : ")
          .append(s.getPrixBase())
          .append(" MAD");
    }

    return sb.toString();
}
private String getHelp() {
    return "👋 Je peux vous aider avec :"
            + "\n📦 Commandes"
            + "\n📦 Articles déposés"
            + "\n🚚 Livraisons"
            + "\n🛠 Services"
            + "\n👤 Profil utilisateur";
}
}
