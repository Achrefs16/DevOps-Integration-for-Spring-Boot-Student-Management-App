package com.example.students.service;

import com.example.students.model.Etudiant;
import com.example.students.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EtudiantService {
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }
    
    public Optional<Etudiant> getEtudiantById(Long id) {
        return etudiantRepository.findById(id);
    }
    
    public Etudiant createEtudiant(Etudiant etudiant) {
        if (etudiantRepository.existsByEmail(etudiant.getEmail())) {
            throw new RuntimeException("Un étudiant avec cet email existe déjà");
        }
        return etudiantRepository.save(etudiant);
    }
    
    public Etudiant updateEtudiant(Long id, Etudiant etudiantDetails) {
        Etudiant etudiant = etudiantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'id: " + id));
        
        etudiant.setNom(etudiantDetails.getNom());
        etudiant.setPrenom(etudiantDetails.getPrenom());
        etudiant.setEmail(etudiantDetails.getEmail());
        etudiant.setNiveau(etudiantDetails.getNiveau());
        
        return etudiantRepository.save(etudiant);
    }
    
    public void deleteEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Étudiant non trouvé avec l'id: " + id));
        
        etudiantRepository.delete(etudiant);
    }
}
