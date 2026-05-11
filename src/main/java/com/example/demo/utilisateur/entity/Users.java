package com.example.demo.utilisateur.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.example.demo.utilisateur.entity.Users;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")

public class  Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    @Column(name = "birth_date")
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate birthDate;
    private String email;

    @Column(name = "pass_word")
    private String passWord;

    private String telephone;
    private String cin;

    @Embedded
    private Adresse adresse;
 private boolean enabled = true;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ROLE_CLIENT, ROLE_ADMIN, ROLE_LIVREUR
  
}
