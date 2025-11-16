package com.example.students.controller;

import com.example.students.model.Etudiant;
import com.example.students.service.EtudiantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtudiantController.class)
class EtudiantControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EtudiantService etudiantService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
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
    void testGetAllEtudiants() throws Exception {
        when(etudiantService.getAllEtudiants()).thenReturn(Arrays.asList(etudiant));
        
        mockMvc.perform(get("/etudiants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nom").value("Dupont"))
            .andExpect(jsonPath("$[0].email").value("jean.dupont@example.com"));
        
        verify(etudiantService, times(1)).getAllEtudiants();
    }
    
    @Test
    void testGetEtudiantById_Success() throws Exception {
        when(etudiantService.getEtudiantById(1L)).thenReturn(Optional.of(etudiant));
        
        mockMvc.perform(get("/etudiants/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom").value("Dupont"))
            .andExpect(jsonPath("$.prenom").value("Jean"));
    }
    
    @Test
    void testGetEtudiantById_NotFound() throws Exception {
        when(etudiantService.getEtudiantById(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/etudiants/999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void testCreateEtudiant_Success() throws Exception {
        when(etudiantService.createEtudiant(any(Etudiant.class))).thenReturn(etudiant);
        
        mockMvc.perform(post("/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etudiant)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nom").value("Dupont"))
            .andExpect(jsonPath("$.email").value("jean.dupont@example.com"));
        
        verify(etudiantService, times(1)).createEtudiant(any(Etudiant.class));
    }
    
    @Test
    void testCreateEtudiant_ValidationError() throws Exception {
        Etudiant invalidEtudiant = new Etudiant();
        // Pas de nom, prénom, email, niveau
        
        mockMvc.perform(post("/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEtudiant)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testUpdateEtudiant_Success() throws Exception {
        when(etudiantService.updateEtudiant(eq(1L), any(Etudiant.class))).thenReturn(etudiant);
        
        mockMvc.perform(put("/etudiants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etudiant)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom").value("Dupont"));
    }
    
    @Test
    void testUpdateEtudiant_NotFound() throws Exception {
        when(etudiantService.updateEtudiant(eq(999L), any(Etudiant.class)))
            .thenThrow(new RuntimeException("Étudiant non trouvé"));
        
        mockMvc.perform(put("/etudiants/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etudiant)))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteEtudiant_Success() throws Exception {
        doNothing().when(etudiantService).deleteEtudiant(1L);
        
        mockMvc.perform(delete("/etudiants/1"))
            .andExpect(status().isNoContent());
        
        verify(etudiantService, times(1)).deleteEtudiant(1L);
    }
    
    @Test
    void testDeleteEtudiant_NotFound() throws Exception {
        doThrow(new RuntimeException("Étudiant non trouvé"))
            .when(etudiantService).deleteEtudiant(999L);
        
        mockMvc.perform(delete("/etudiants/999"))
            .andExpect(status().isNotFound());
    }
}
