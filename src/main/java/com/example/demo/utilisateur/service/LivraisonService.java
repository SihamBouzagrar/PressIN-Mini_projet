package com.example.demo.utilisateur.service;
import com.example.demo.utilisateur.entity.*;
import com.example.demo.utilisateur.repository.CommandeRepository;
import com.example.demo.utilisateur.repository.LivraisonRepository;
import com.example.demo.utilisateur.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final UserRepository userRepository;
    private final CommandeRepository commandeRepository; // ← instance, pas static

    public Livraison creerLivraison(Long commandeId, Long livreurId) {

        Commande commande = commandeRepository.findById(commandeId) // ← instance
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        Users livreur = userRepository.findById(livreurId) // ← instance
                .filter(u -> u.getRole() == Role.ROLE_LIVREUR)
                .orElseThrow(() -> new RuntimeException("Livreur introuvable"));

        Livraison livraison = Livraison.builder()
                .commande(commande)
                .livreur(livreur)
                .statut(Livraison.StatutLivraison.PLANIFIEE)
                .dateLivraisonPrevue(LocalDateTime.now().plusDays(1))
                .build();

        return livraisonRepository.save(livraison);
    }

    public Livraison changerStatut(Long id, Livraison.StatutLivraison nouveauStatut) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable"));

        livraison.setStatut(nouveauStatut);

        if (nouveauStatut == Livraison.StatutLivraison.COLLECTEE)
            livraison.setDateCollecteEffective(LocalDateTime.now());

        if (nouveauStatut == Livraison.StatutLivraison.LIVREE)
            livraison.setDateLivraisonEffective(LocalDateTime.now());

        return livraisonRepository.save(livraison);
    }

    public Livraison mettreAJourDates(Long id,
                                       LocalDateTime dateCollecteEffective,
                                       LocalDateTime dateLivraisonEffective) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison introuvable"));

        if (dateCollecteEffective != null)
            livraison.setDateCollecteEffective(dateCollecteEffective);
        if (dateLivraisonEffective != null)
            livraison.setDateLivraisonEffective(dateLivraisonEffective);

        return livraisonRepository.save(livraison);
    }

    public List<Livraison> findByLivreur(Long livreurId) {
        return livraisonRepository.findByLivreurId(livreurId);
    }

    public Optional<Livraison> findByCommande(Long commandeId) {
        return livraisonRepository.findByCommandeId(commandeId);
    }

    public List<Livraison> findAll() {
        return livraisonRepository.findAll();
    }

    public List<Livraison> findByStatut(Livraison.StatutLivraison statut) {
        return livraisonRepository.findByStatut(statut);
    }
}