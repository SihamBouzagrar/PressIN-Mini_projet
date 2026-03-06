package com.example.demo.utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.utilisateur.entity.Commande;

import java.util.List;
import java.util.Optional;



public interface CommandeRepository extends JpaRepository<Commande, Long> {
List<Commande> findByClientId(Long clientId);
   
    List<Commande> findByLivreurId(Long livreurId);

    // ✅ statut
    List<Commande> findByStatut(Commande.StatutCommande statut);
    List<Commande> findByClientIdAndStatut(Long clientId, Commande.StatutCommande statut);

    // ✅ count par client
    long countByClientId(Long clientId);
    long countByClientIdAndStatut(Long clientId, Commande.StatutCommande statut);
    Optional<Commande> findById(Long id);

    // ❌ SUPPRIMER ces méthodes — "user" n'existe pas dans Commande
    // long countByUserIdAndStatutIgnoreCase(Integer userId, String statut);
    // List<Commande> findByUserId(Integer userId);
}