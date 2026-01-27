package com.example.demo.utilisateur.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.utilisateur.entity.Commande;
import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {

    // Commandes totales pour un utilisateur
    long countByUserId(Integer userId);

    // Commandes livrées pour un utilisateur
    long countByUserIdAndStatut(Integer userId, String statut);

    // Commandes en cours pour un utilisateur (ignore case)
    long countByUserIdAndStatutIgnoreCase(Integer userId, String statut);

    // Montant total pour un utilisateur
    @Query("SELECT COALESCE(SUM(c.prixTotal),0) FROM Commande c WHERE c.user.id = :userId")
    Double sumPrixTotalByUserId(@Param("userId") Integer userId);

    // Liste de commandes par utilisateur
    List<Commande> findByUserId(Integer userId);
}