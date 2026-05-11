package com.example.demo.utilisateur.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.utilisateur.entity.Livraison;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    List<Livraison> findByLivreurId(Long livreurId);
    Optional<Livraison> findByCommandeId(Long commandeId);
    List<Livraison> findByStatut(Livraison.StatutLivraison statut);
}