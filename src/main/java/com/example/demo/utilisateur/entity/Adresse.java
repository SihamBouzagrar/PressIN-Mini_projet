package com.example.demo.utilisateur.entity;

import javax.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
// ✅ Supprimer @Builder — cause des conflits avec la valeur par défaut "Maroc"
public class Adresse {
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;   // ✅ valeur par défaut gérée dans le constructeur


    }
