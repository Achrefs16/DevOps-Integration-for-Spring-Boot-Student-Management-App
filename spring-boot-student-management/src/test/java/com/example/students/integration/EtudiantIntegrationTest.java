package com.example.students.integration;

import com.example.students.model.Etudiant;
import com.example.students.repository.EtudiantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EtudiantIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() {
        etudiantRepository.deleteAll();
    }
    
    @Test
    void testCompleteEtudiantLifecycle() throws Exception {
        // 1. Créer un étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Martin");
        etudiant.setPrenom("Sophie");
        etudiant.setEmail("sophie.martin@example.com");
        etudiant.setNiveau("M1");
        
        MvcResult createResult = mockMvc.perform(post("/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etudiant)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nom").value("Martin"))
            .andExpect(jsonPath("$.prenom").value("Sophie"))
            .andReturn();
        
        String responseBody = createResult.getResponse().getContentAsString();
        Etudiant createdEtudiant = objectMapper.readValue(responseBody, Etudiant.class);
        Long etudiantId = createdEtudiant.getId();
        
        // 2. Récupérer l'étudiant par ID
        mockMvc.perform(get("/etudiants/" + etudiantId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom").value("Martin"))
            .andExpect(jsonPath("$.email").value("sophie.martin@example.com"));
        
        // 3. Lister tous les étudiants
        mockMvc.perform(get("/etudiants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(1));
        
        // 4. Mettre à jour l'étudiant
        createdEtudiant.setNiveau("M2");
        mockMvc.perform(put("/etudiants/" + etudiantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdEtudiant)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.niveau").value("M2"));
        
        // 5. Supprimer l'étudiant
        mockMvc.perform(delete("/etudiants/" + etudiantId))
            .andExpect(status().isNoContent());
        
        // 6. Vérifier que l'étudiant n'existe plus
        mockMvc.perform(get("/etudiants/" + etudiantId))
            .andExpect(status().isNotFound());
        
        // 7. Vérifier que la liste est vide
        mockMvc.perform(get("/etudiants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    void testCreateMultipleEtudiants() throws Exception {
        // Créer plusieurs étudiants
        for (int i = 1; i <= 3; i++) {
            Etudiant etudiant = new Etudiant();
            etudiant.setNom("Etudiant" + i);
            etudiant.setPrenom("Prenom" + i);
            etudiant.setEmail("etudiant" + i + "@example.com");
            etudiant.setNiveau("L" + i);
            
            mockMvc.perform(post("/etudiants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isCreated());
        }
        
        // Vérifier qu'il y a bien 3 étudiants
        mockMvc.perform(get("/etudiants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3));
    }
    
    @Test
    void testCreateEtudiantWithDuplicateEmail() throws Exception {
        // Créer le premier étudiant
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNom("Dupont");
        etudiant1.setPrenom("Jean");
        etudiant1.setEmail("same@example.com");
        etudiant1.setNiveau("L1");
        
        mockMvc.perform(post("/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etudiant1)))
            .andExpect(status().isCreated());
        
        // Tenter de créer un deuxième étudiant avec le même email
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setNom("Martin");
        etudiant2.setPrenom("Sophie");
        etudiant2.setEmail("same@example.com");
        etudiant2.setNiveau("L2");
        
        mockMvc.perform(post("/etudiants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etudiant2)))
            .andExpect(status().isBadRequest());
    }
}
