package com.example.demo.utilisateur.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paiements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;

    @Column(nullable = false)
    private Double montant;

   // private String reference; // référence transaction

    @Column(updatable = false)
    private LocalDateTime datePaiement;

    @PrePersist
    protected void onCreate() {
        this.datePaiement = LocalDateTime.now();
        this.statut = StatutPaiement.EN_ATTENTE;
    }

    public enum ModePaiement {
        ESPECES, CARTE_BANCAIRE, VIREMENT, MOBILE_PAYMENT
    }

    public enum StatutPaiement {
        EN_ATTENTE, PAYE, REMBOURSE, ECHOUE
    }
}
