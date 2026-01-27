package com.example.demo.utilisateur.entity;

import java.time.LocalDate;
import javax.persistence.*;

import org.springframework.lang.NonNull;


@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dateReception;
    private LocalDate dateRemisePrevue;

    @ManyToOne
    @JoinColumn(name = "id_users", nullable = false)
    private Users user;

    @NonNull
    @Column(name = "statut", nullable = false)
    private String statut;

  
    private Double prixTotal;

    private String codeOcr;

    // === Constructeurs ===
    public Commande() {}

    public Commande(LocalDate dateReception, LocalDate dateRemisePrevue, Users user, String statut,
                     Double prixTotal, String codeOcr) {
        this.dateReception = dateReception;
        this.dateRemisePrevue = dateRemisePrevue;
        this.user = user;
        this.statut = statut;
        this.prixTotal = prixTotal;
        this.codeOcr = codeOcr;
    }

    // === Getters & Setters ===
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDateReception() { return dateReception; }
    public void setDateReception(LocalDate dateReception) { this.dateReception = dateReception; }

    public LocalDate getDateRemisePrevue() { return dateRemisePrevue; }
    public void setDateRemisePrevue(LocalDate dateRemisePrevue) { this.dateRemisePrevue = dateRemisePrevue; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }

    public String getCodeOcr() { return codeOcr; }
    public void setCodeOcr(String codeOcr) { this.codeOcr = codeOcr; }
}