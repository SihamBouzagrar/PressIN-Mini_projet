package com.example.demo.dto;

import com.example.demo.utilisateur.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String passWord;
    private String telephone;
    private String cin;
    private Role role; // ROLE_CLIENT, ROLE_ADMIN, ROLE_LIVREUR
}