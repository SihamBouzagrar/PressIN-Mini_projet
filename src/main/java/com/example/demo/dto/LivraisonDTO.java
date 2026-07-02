package com.example.demo.dto;

import com.example.demo.utilisateur.entity.Livraison;
import com.example.demo.utilisateur.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivraisonDTO {

    private Long id;
    private Long commandeId;
    private String statut;
    
    // Dates
    private String dateCollectePrevue;
    private String dateCollecteEffective;
    private String dateLivraisonPrevue;
    private String dateLivraisonEffective;
    
    // Adresse de collecte (Embedded)
    private String collecteRue;
    private String collecteVille;
    private String collecteCodePostal;
    private String collectePays;
    
    // Adresse de destination (Embedded)
    private String destRue;
    private String destVille;
    private String destCodePostal;
    private String destPays;
    
    // 👤 Info client
    private ClientInfo client;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClientInfo {
        private String firstname;
        private String lastname;
        private String telephone;
        private String email;
        // Adresse client
        private String rue;
        private String ville;
        private String codePostal;
    }

    public static LivraisonDTO from(Livraison l) {
        if (l == null) return null;
        
        LivraisonDTO dto = new LivraisonDTO();
        dto.setId(l.getId());
        dto.setStatut(l.getStatut() != null ? l.getStatut().name() : null);
        
        // Commande
        if (l.getCommande() != null) {
            dto.setCommandeId(l.getCommande().getId());
            
            // 👤 CLIENT - Users (avec s)
            Users client = l.getCommande().getClient();
            if (client != null) {
                ClientInfo ci = new ClientInfo();
                ci.setFirstname(client.getFirstname());
                ci.setLastname(client.getLastname());
                ci.setTelephone(client.getTelephone());
                ci.setEmail(client.getEmail());
                
                // Adresse du client (Embedded)
                if (client.getAdresse() != null) {
                    ci.setRue(client.getAdresse().getRue());
                    ci.setVille(client.getAdresse().getVille());
                    ci.setCodePostal(client.getAdresse().getCodePostal());
                }
                
                dto.setClient(ci);
            }
        }
        
        // Dates
        if (l.getDateCollectePrevue() != null) {
            dto.setDateCollectePrevue(l.getDateCollectePrevue().toString());
        }
        if (l.getDateCollecteEffective() != null) {
            dto.setDateCollecteEffective(l.getDateCollecteEffective().toString());
        }
        if (l.getDateLivraisonPrevue() != null) {
            dto.setDateLivraisonPrevue(l.getDateLivraisonPrevue().toString());
        }
        if (l.getDateLivraisonEffective() != null) {
            dto.setDateLivraisonEffective(l.getDateLivraisonEffective().toString());
        }
        
        // Adresse de collecte (Embedded)
        if (l.getAdresseCollecte() != null) {
            dto.setCollecteRue(l.getAdresseCollecte().getRue());
            dto.setCollecteVille(l.getAdresseCollecte().getVille());
            dto.setCollecteCodePostal(l.getAdresseCollecte().getCodePostal());
            dto.setCollectePays(l.getAdresseCollecte().getPays());
        }
        
        // Adresse de livraison (Embedded)
        if (l.getAdresseLivraison() != null) {
            dto.setDestRue(l.getAdresseLivraison().getRue());
            dto.setDestVille(l.getAdresseLivraison().getVille());
            dto.setDestCodePostal(l.getAdresseLivraison().getCodePostal());
            dto.setDestPays(l.getAdresseLivraison().getPays());
        }
        
        return dto;
    }
}