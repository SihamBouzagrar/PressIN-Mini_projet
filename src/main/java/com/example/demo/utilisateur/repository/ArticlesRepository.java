package com.example.demo.utilisateur.repository;

import com.example.demo.utilisateur.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Integer> {
    // findAll() est déjà fourni par JpaRepository
}
