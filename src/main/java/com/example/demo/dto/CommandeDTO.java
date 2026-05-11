package com.example.demo.dto; 
import lombok.*;
import java.time.LocalDate;

import com.example.demo.utilisateur.entity.Adresse;
import com.example.demo.utilisateur.entity.Commande.StatutCommande;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CommandeDTO {
    private LocalDate dateCollecteSouhaitee;
    private LocalDate dateLivraisonSouhaitee;
    private String    typeLivraison;
    private Double    montantRemise;
    private Adresse   adresseLivraison;  // ✅ doit être présent
    private StatutCommande statut;
}
