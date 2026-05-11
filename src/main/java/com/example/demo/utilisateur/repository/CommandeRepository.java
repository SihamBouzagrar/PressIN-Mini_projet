package com.example.demo.utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.utilisateur.entity.Commande;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByClientId(Long clientId);

    List<Commande> findByLivreurId(Long livreurId);

    List<Commande> findByStatut(Commande.StatutCommande statut);
    
}