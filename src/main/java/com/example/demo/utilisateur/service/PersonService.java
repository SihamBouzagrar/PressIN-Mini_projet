package com.example.demo.utilisateur.service;

import com.example.demo.utilisateur.entity.Users;
import com.example.demo.utilisateur.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /*
     * ============================
     * CRUD DE BASE
     * ============================
     */

    // Récupérer tous les utilisateurs
    public List<Users> trouverToutesLesPersonnes() {
        return personRepository.findAll();
    }

    // Récupérer un utilisateur par ID
    public Optional<Users> findPersonById(Integer id) {
        return personRepository.findById(id);
    }

    // Créer ou mettre à jour un utilisateur
    public Users saveUser(Users user) {
        return personRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deletePerson(Integer id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id : " + id);
        }
        personRepository.deleteById(id);
    }

    /*
     * ============================
     * RECHERCHES
     * ============================
     */

    public Optional<Users> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Optional<Users> findByCin(String cin) {
        return personRepository.findByCin(cin);
    }

    public List<Users> findByFirstname(String firstname) {
        return personRepository.findByFirstname(firstname);
    }

    public List<Users> findByLastname(String lastname) {
        return personRepository.findByLastname(lastname);
    }

    /*
     * ============================
     * VALIDATIONS
     * ============================
     */

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public boolean existsByCin(String cin) {
        return personRepository.existsByCin(cin);
    }

    /*
     * ============================
     * UTILITAIRES
     * ============================
     */

    public long countUsers() {
        return personRepository.count();
    }

    public boolean existsById(Integer id) {
        return personRepository.existsById(id);
    }

    // Sauvegarder un utilisateur
    public Users savePerson(Users person) {
        return personRepository.save(person);
    }

    // Récupérer tous les utilisateurs
    public List<Users> findAllUsers() {
        return personRepository.findAll();
    }

}
