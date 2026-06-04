package com.example.demo.dto; 
import lombok.*;
import java.time.LocalDate;
import java.util.List;

import com.example.demo.utilisateur.entity.AdresseLivraison;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class CommandeDTO {
    private LocalDate dateCollecteSouhaitee;
    private LocalDate dateLivraisonSouhaitee;
    private Double montantTotal;
    private String typeLivraison;
    private Double montantRemise;
    private AdresseLivraison adresseLivraison;
    private String statut;
    
    private List<ArticleDTO> articles;
}