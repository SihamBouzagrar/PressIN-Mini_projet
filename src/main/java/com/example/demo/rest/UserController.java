package com.example.demo.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.utilisateur.entity.Role;
import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/rest/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
// ===== GET ALL PAR ROLE =====
    @GetMapping("/clients")
    public ResponseEntity<List<Users>> getAllClients() {
        return ResponseEntity.ok(userService.findByRole(Role.ROLE_CLIENT));
    }

    @GetMapping("/admins")
    public ResponseEntity<List<Users>> getAllAdmins() {
        return ResponseEntity.ok(userService.findByRole(Role.ROLE_ADMIN));
    }

    @GetMapping("/livreurs")
    public ResponseEntity<List<Users>> getAllLivreurs() {
        return ResponseEntity.ok(userService.findByRole(Role.ROLE_LIVREUR));
    }
 // ===== GET BY ID =====
    @GetMapping("/{id}")
    public ResponseEntity<Users> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
// ===== UPDATE =====
    @PutMapping("/update/{id}")
    public ResponseEntity<Users> update(
            @PathVariable Long id,
            @RequestBody Users userData) {
        return userService.findById(id)
                .map(user -> {
                    if (userData.getFirstname() != null)
                        user.setFirstname(userData.getFirstname());
                    if (userData.getLastname() != null)
                        user.setLastname(userData.getLastname());
                    if (userData.getTelephone() != null)
                        user.setTelephone(userData.getTelephone());
                    if (userData.getAdresse() != null)
                        user.setAdresse(userData.getAdresse());
                     if (userData.getBirthDate() != null)
                    user.setBirthDate(userData.getBirthDate());
                    if (userData.getCin() != null)
                        user.setCin(userData.getCin());
                    return ResponseEntity.ok(userService.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

// ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
