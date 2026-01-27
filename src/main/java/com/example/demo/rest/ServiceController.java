package com.example.demo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.utilisateur.entity.MonService;
import com.example.demo.utilisateur.service.ServiceServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/rest/services")
@CrossOrigin("*")
public class ServiceController {

    private final ServiceServiceImpl serviceService;

    public ServiceController(ServiceServiceImpl serviceService) {
        this.serviceService = serviceService;
    }

    // GET - Tous les services
    @GetMapping("/all")
    public ResponseEntity<List<MonService>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    // GET - Service par ID
    @GetMapping("/{id}")
    public ResponseEntity<MonService> getServiceById(@PathVariable Integer id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Créer un service
    @PostMapping("/create")
    public ResponseEntity<MonService> createService(@RequestBody MonService service) {
        MonService saved = serviceService.saveService(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT - Modifier un service
    @PutMapping("/update/{id}")
    public ResponseEntity<MonService> updateService(
            @PathVariable Integer id,
            @RequestBody MonService serviceDetails) {

        MonService updated = serviceService.updateService(id, serviceDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE - Supprimer un service
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}