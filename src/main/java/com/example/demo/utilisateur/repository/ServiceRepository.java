package com.example.demo.utilisateur.repository;

import com.example.demo.utilisateur.entity.MonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<MonService, Integer> {
    // Tu peux ajouter des méthodes custom si besoin
}