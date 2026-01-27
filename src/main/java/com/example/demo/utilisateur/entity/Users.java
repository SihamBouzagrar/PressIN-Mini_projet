package com.example.demo.utilisateur.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

    @Column(name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Column(name = "cin", unique = true)
    private String cin;

    @Column(name = "pass_word", nullable = false)
    private String passWord;

    @Column(name = "role")
    private String role;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    // ⚡ Méthode exécutée avant insertion pour initialiser la date de création
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
