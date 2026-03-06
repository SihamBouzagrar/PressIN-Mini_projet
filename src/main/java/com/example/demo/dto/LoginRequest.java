package com.example.demo.dto;

import lombok.Data;


@Data
public class LoginRequest {
private String email;
private String passWord; // attention à la casse, doit matcher le JSON envoyé depuis le frontend
}