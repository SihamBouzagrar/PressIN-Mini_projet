package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.service.PersonService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/person")

public class PersonController {

    @Autowired
    private PersonService personService;
      // ======= POST - Créer un utilisateur =======
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Users> createPerson(@RequestBody Users person) {
        Users savedUser = personService.savePerson(person);
        return ResponseEntity.ok(savedUser);
    }


    /// GET - tous les users
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(personService.findAllUsers());
    }

    // GET - Récupérer un utilisateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        Optional<Users> user = personService.findPersonById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Rechercher par email
    @GetMapping("/email/{email}")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        Optional<Users> user = personService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Rechercher par CIN
    @GetMapping("/cin/{cin}")
    public ResponseEntity<Users> getUserByCin(@PathVariable String cin) {
        Optional<Users> user = personService.findByCin(cin);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Rechercher par prénom
    @GetMapping("/firstname/{firstname}")
    public List<Users> getUsersByFirstname(@PathVariable String firstname) {
        return personService.findByFirstname(firstname);
    }

    // GET - Rechercher par nom
    @GetMapping("/lastname/{lastname}")
    public List<Users> getUsersByLastname(@PathVariable String lastname) {
        return personService.findByLastname(lastname);
    }

    // GET - Vérifier si un email existe
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(personService.existsByEmail(email));
    }

    // GET - Compter les utilisateurs
    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        return ResponseEntity.ok(personService.countUsers());
    }
}