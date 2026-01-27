package com.example.demo.utilisateur.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.utilisateur.entity.MonService;
import com.example.demo.utilisateur.repository.ServiceRepository;

@Service
public class ServiceServiceImpl {

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<MonService> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<MonService> getServiceById(Integer id) {
        return serviceRepository.findById(id);
    }

    public MonService saveService(MonService service) {
        return serviceRepository.save(service);
    }

    public MonService updateService(Integer id, MonService service) {
        return serviceRepository.findById(id)
                .map(existing -> {
                    existing.setNom(service.getNom());
                    existing.setPrix(service.getPrix());
                    existing.setDelaiJours(service.getDelaiJours());
                    return serviceRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Service not found with id " + id));
    }

    public void deleteService(Integer id) {
        serviceRepository.deleteById(id);
    }
}