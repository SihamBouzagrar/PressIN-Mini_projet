package com.example.demo.utilisateur.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.utilisateur.entity.Admin;
import com.example.demo.utilisateur.entity.Client;
import com.example.demo.utilisateur.entity.Livreur;
import com.example.demo.utilisateur.repository.AdminRepository;
import com.example.demo.utilisateur.repository.ClientRepository;
import com.example.demo.utilisateur.repository.LivreurRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ClientRepository  clientRepository;
    private final AdminRepository   adminRepository;
    private final LivreurRepository livreurRepository;

    // ===== CLIENTS =====
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> findClientById(Long clientId) {
        return clientRepository.findById(clientId);
    }

    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    public boolean clientExistsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public long countClients() {
        return clientRepository.count();
    }

    // ===== ADMINS =====
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }

    // ===== LIVREURS =====
    public List<Livreur> findAllLivreurs() {
        return livreurRepository.findAll();
    }

    public Optional<Livreur> findLivreurById(Long id) {
        return livreurRepository.findById(id);
    }

    public Livreur saveLivreur(Livreur livreur) {
        return livreurRepository.save(livreur);
    }

    public void deleteLivreurById(Long id) {
        livreurRepository.deleteById(id);
    }
}
