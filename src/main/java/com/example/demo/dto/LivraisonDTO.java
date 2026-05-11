// LivraisonDTO.java
package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.utilisateur.entity.Livraison;
import com.example.demo.utilisateur.entity.Livraison.StatutLivraison;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LivraisonDTO {
    private Long id;
    private Long commandeId;
    private String livreurNom;
    private String livreurPrenom;
    private Long livreurId;
    private StatutLivraison statut;
    private LocalDateTime dateCollectePrevue;
    private LocalDateTime dateLivraisonPrevue;

    public static LivraisonDTO from(Livraison l) {
        return LivraisonDTO.builder()
            .id(l.getId())
            .commandeId(l.getCommande().getId())
            .livreurId(l.getLivreur().getId())
            .livreurNom(l.getLivreur().getLastname())
            .livreurPrenom(l.getLivreur().getFirstname())
            .statut(l.getStatut())
            .dateCollectePrevue(l.getDateCollectePrevue())
            .dateLivraisonPrevue(l.getDateLivraisonPrevue())
            .build();
    }
}