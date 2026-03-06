package com.example.demo.utilisateur.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.utilisateur.entity.Livreur;

public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    
}
