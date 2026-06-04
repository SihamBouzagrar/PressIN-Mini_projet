package com.example.demo.utilisateur.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.utilisateur.entity.ServicePressing;
import com.example.demo.utilisateur.entity.ServicePressing.CategorieService;

@Repository
public interface ServiceRepository extends JpaRepository<ServicePressing, Long > {

   
    List<ServicePressing> findByPrixBaseLessThanEqual(Double prixMax);
   
    List<ServicePressing> findByNomContainingIgnoreCase(String nom);

    List<ServicePressing> findByCategorie(CategorieService categorie);
}
