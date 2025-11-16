package com.example.students.controller;

import com.example.students.model.Etudiant;
import com.example.students.service.EtudiantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/etudiants")
@CrossOrigin(origins = "*")
public class EtudiantController {
    
    @Autowired
    private EtudiantService etudiantService;
    
    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long id) {
        return etudiantService.getEtudiantById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Etudiant> createEtudiant(@Valid @RequestBody Etudiant etudiant) {
        try {
            Etudiant nouveauEtudiant = etudiantService.createEtudiant(etudiant);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauEtudiant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Etudiant> updateEtudiant(
            @PathVariable Long id, 
            @Valid @RequestBody Etudiant etudiantDetails) {
        try {
            Etudiant updatedEtudiant = etudiantService.updateEtudiant(id, etudiantDetails);
            return ResponseEntity.ok(updatedEtudiant);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        try {
            etudiantService.deleteEtudiant(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
