package com.example.demo.dto;

import lombok.Data;


@Data
public class LoginRequest {
    private String email;
    private String passWord; // ✔ doit matcher ton backend

    public LoginRequest() {}

    public String getEmail() { return email; }
    public String getPassWord() { return passWord; }

    public void setEmail(String email) { this.email = email; }
    public void setPassWord(String passWord) { this.passWord = passWord; }
}