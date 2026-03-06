package com.example.demo.dto; 
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {

    private Double montantRemise;
    private String typeLivraison;  // "A_DOMICILE" ou "EN_MAGASIN"
    private LocalDate dateCollecteSouhaitee;
    private LocalDate dateLivraisonSouhaitee;

    // Adresse livraison
    private String livraisonRue;
    private String livraisonVille;
    private String livraisonCodePostal;
    private String livraisonPays = "Maroc";;
 

    // Anciens champs conservés
    private Double prixTotal;
    private String orderDetails;
}
