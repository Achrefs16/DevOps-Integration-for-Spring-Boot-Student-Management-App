package com.example.students.service;

import com.example.students.model.Etudiant;
import com.example.students.repository.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {
    
    @Mock
    private EtudiantRepository etudiantRepository;
    
    @InjectMocks
    private EtudiantService etudiantService;
    
    private Etudiant etudiant;
    
    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNom("Dupont");
        etudiant.setPrenom("Jean");
        etudiant.setEmail("jean.dupont@example.com");
        etudiant.setNiveau("L3");
    }
    
    @Test
    void testGetAllEtudiants() {
        // Given
        List<Etudiant> etudiants = Arrays.asList(etudiant);
        when(etudiantRepository.findAll()).thenReturn(etudiants);
        
        // When
        List<Etudiant> result = etudiantService.getAllEtudiants();
        
        // Then
        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).getNom());
        verify(etudiantRepository, times(1)).findAll();
    }
    
    @Test
    void testGetEtudiantById_Success() {
        // Given
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        
        // When
        Optional<Etudiant> result = etudiantService.getEtudiantById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals("Dupont", result.get().getNom());
        assertEquals("jean.dupont@example.com", result.get().getEmail());
    }
    
    @Test
    void testGetEtudiantById_NotFound() {
        // Given
        when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        Optional<Etudiant> result = etudiantService.getEtudiantById(999L);
        
        // Then
        assertFalse(result.isPresent());
    }
    
    @Test
    void testCreateEtudiant_Success() {
        // Given
        when(etudiantRepository.existsByEmail(anyString())).thenReturn(false);
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);
        
        // When
        Etudiant result = etudiantService.createEtudiant(etudiant);
        
        // Then
        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
    }
    
    @Test
    void testCreateEtudiant_EmailAlreadyExists() {
        // Given
        when(etudiantRepository.existsByEmail(anyString())).thenReturn(true);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            etudiantService.createEtudiant(etudiant);
        });
        
        assertEquals("Un étudiant avec cet email existe déjà", exception.getMessage());
        verify(etudiantRepository, never()).save(any(Etudiant.class));
    }
    
    @Test
    void testUpdateEtudiant_Success() {
        // Given
        Etudiant updatedDetails = new Etudiant();
        updatedDetails.setNom("Martin");
        updatedDetails.setPrenom("Sophie");
        updatedDetails.setEmail("sophie.martin@example.com");
        updatedDetails.setNiveau("M1");
        
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);
        
        // When
        Etudiant result = etudiantService.updateEtudiant(1L, updatedDetails);
        
        // Then
        assertNotNull(result);
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
    }
    
    @Test
    void testUpdateEtudiant_NotFound() {
        // Given
        when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            etudiantService.updateEtudiant(999L, etudiant);
        });
    }
    
    @Test
    void testDeleteEtudiant_Success() {
        // Given
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        doNothing().when(etudiantRepository).delete(any(Etudiant.class));
        
        // When
        etudiantService.deleteEtudiant(1L);
        
        // Then
        verify(etudiantRepository, times(1)).delete(any(Etudiant.class));
    }
    
    @Test
    void testDeleteEtudiant_NotFound() {
        // Given
        when(etudiantRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            etudiantService.deleteEtudiant(999L);
        });
    }
}
