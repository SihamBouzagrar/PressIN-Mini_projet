package com.example.demo.utilisateur.service;


import com.example.demo.utilisateur.entity.ServicePressing;
import com.example.demo.utilisateur.repository.ServiceRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class serviceService {
@Autowired
    private final ServiceRepository serviceRepository;

   public List<ServicePressing> getAllServices() {
        return serviceRepository.findAll();
    }

   public Optional<ServicePressing> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }
    
     // GET - Par catégorie
    public List<ServicePressing> getByCategorie(ServicePressing.CategorieService categorie) {
        return serviceRepository.findByCategorie(categorie);
    }

    // GET - Recherche par nom
    public List<ServicePressing> searchByNom(String nom) {
        return serviceRepository.findByNomContainingIgnoreCase(nom);
    }

    // GET - Par prix max
    public List<ServicePressing> getByPrixMax(Double prixMax) {
        return serviceRepository.findByPrixBaseLessThanEqual(prixMax);
    }
     // POST - Créer
   public ServicePressing saveService(ServicePressing service) {
        return serviceRepository.save(service);
    }

   // PUT - Modifier
   public ServicePressing updateService(Long id, ServicePressing serviceDetails) {
        ServicePressing service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service introuvable id=" + id));

        if (serviceDetails.getNom() != null)
            service.setNom(serviceDetails.getNom());
        if (serviceDetails.getDescription() != null)
            service.setDescription(serviceDetails.getDescription());
        if (serviceDetails.getCategorie() != null)
            service.setCategorie(serviceDetails.getCategorie());
        if (serviceDetails.getPrixBase() != null)
            service.setPrixBase(serviceDetails.getPrixBase());
        if (serviceDetails.getDureeEstimeeHeures() != null)
            service.setDureeEstimeeHeures(serviceDetails.getDureeEstimeeHeures());
        if (serviceDetails.getOptions() != null && !serviceDetails.getOptions().isEmpty())
            service.setOptions(serviceDetails.getOptions());

        return serviceRepository.save(service);
    }
    // DELETE
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

   
}