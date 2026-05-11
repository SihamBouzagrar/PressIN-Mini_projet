package com.example.demo.rest;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.utilisateur.entity.Role;
import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Builder
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userRepository.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassWord(), u.getPassWord()))
                .map(u -> ResponseEntity.ok(u))
                .orElse(ResponseEntity.status(401).body(null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email déjà utilisé");
        }

        Users user = Users.builder()
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .email(req.getEmail())
                .passWord(passwordEncoder.encode(req.getPassWord())) // ← hashé
                .telephone(req.getTelephone())
                .cin(req.getCin())
                .role(req.getRole())
                .enabled(true)
                .build();
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new RuntimeException("Interdit");
        }
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Inscription réussie"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        // Sans session ni JWT, le logout est géré côté frontend
        return ResponseEntity.ok(Map.of("message", "Déconnecté avec succès"));
    }
}