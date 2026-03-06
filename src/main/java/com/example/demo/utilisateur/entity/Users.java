package com.example.demo.utilisateur.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)

@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "pass_word", nullable = false)
    private String passWord;
    @Column(unique = true)
    private String telephone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "cin", unique = true)
    private String cin;

     @Embedded
    private Adresse adresse;
        // Colonne discriminante lue en lecture seule
    @Enumerated(EnumType.STRING)
    @Column(name = "type_utilisateur", insertable = false, updatable = false)
    private TypeUtilisateur typeUtilisateur;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

  

     @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
      
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    public enum TypeUtilisateur {
        ADMIN, CLIENT, LIVREUR
    }
    }

