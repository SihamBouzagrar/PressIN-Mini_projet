package com.example.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.utilisateur.entity.ServicePressing;
import com.example.demo.utilisateur.service.serviceService;

@RestController
@RequestMapping("/rest/services")
@CrossOrigin("*")

public class ServiceController {

   @Autowired                        
    private serviceService serviceService;

    // GET - Tous les services
    @GetMapping("/all")
    public ResponseEntity<List<ServicePressing>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    // GET - Service par ID
    @GetMapping("/{id}")
    public ResponseEntity<ServicePressing> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Créer un service
   @PostMapping("/create")
public ResponseEntity<ServicePressing> createService(@RequestBody ServicePressing service) {
    ServicePressing saved = serviceService.saveService(service);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
}

    // PUT - Modifier un service
  @PutMapping("/update/{id}")
public ResponseEntity<ServicePressing> updateService(
        @PathVariable Long id,
        @RequestBody ServicePressing serviceDetails) {
    ServicePressing updated = serviceService.updateService(id, serviceDetails);
    return ResponseEntity.ok(updated);
}
    // DELETE - Supprimer un service
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}