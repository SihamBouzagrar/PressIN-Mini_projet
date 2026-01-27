package com.example.demo.rest;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.PersonRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final PersonRepository personRepository;

    public AuthController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {

        Optional<Users> user = personRepository.findByEmail(loginRequest.getEmail());

        if(user.isPresent() && user.get().getPassWord().equals(loginRequest.getPassWord())) {
            session.setAttribute("currentUser", user.get());
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("error", "Email ou mot de passe incorrect"));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Déconnecté avec succès"));
    }
}