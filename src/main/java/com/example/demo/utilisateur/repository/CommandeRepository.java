package com.example.demo.utilisateur.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.utilisateur.entity.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
}
