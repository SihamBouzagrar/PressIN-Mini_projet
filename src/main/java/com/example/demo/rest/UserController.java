package com.example.demo.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.utilisateur.entity.Admin;
import com.example.demo.utilisateur.entity.Client;
import com.example.demo.utilisateur.entity.Livreur;
import com.example.demo.utilisateur.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@RestController
@RequestMapping("/rest/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ===== CLIENTS =====
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(userService.findAllClients());
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return userService.findClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/client/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.status(201).body(userService.saveClient(client));
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        userService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    // ===== ADMINS =====
    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(userService.findAllAdmins());
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        return ResponseEntity.status(201).body(userService.saveAdmin(admin));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        userService.deleteAdminById(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LIVREURS =====
    @GetMapping("/livreurs")
    public ResponseEntity<List<Livreur>> getAllLivreurs() {
        return ResponseEntity.ok(userService.findAllLivreurs());
    }

    @PostMapping("/livreur/create")
    public ResponseEntity<Livreur> createLivreur(@RequestBody Livreur livreur) {
        return ResponseEntity.status(201).body(userService.saveLivreur(livreur));
    }

    @DeleteMapping("/livreur/{id}")
    public ResponseEntity<Void> deleteLivreur(@PathVariable Long id) {
        userService.deleteLivreurById(id);
        return ResponseEntity.noContent().build();
    }
}