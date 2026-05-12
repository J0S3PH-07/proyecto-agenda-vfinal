package com.agenda.itic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.itic.dto.ActivitatRequestDTO;
import com.agenda.itic.dto.ActivitatResponseDTO;
import com.agenda.itic.model.Activitat;
import com.agenda.itic.service.ActivitatService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/activitats")
@CrossOrigin(origins = "*")
public class ActivitatController {

    @Autowired
    ActivitatService activitatService;
    @GetMapping("/activitats/model")
    public ResponseEntity<List<Activitat>> getActivitatModel() {
        return ResponseEntity.ok(activitatService.getActivitatModel());
    }
    @GetMapping("/activitats")
    public ResponseEntity<List<ActivitatResponseDTO>> getAllActivitats() {
        return ResponseEntity.status(HttpStatus.OK).body(activitatService.getAllActivitats());
    }

    @GetMapping("/activitat/{id}")
    public ResponseEntity<ActivitatResponseDTO> getActivitatById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(activitatService.getActivitatById(id));
    }

    @GetMapping("/activitats/{idUsuari}")
    public ResponseEntity<List<ActivitatResponseDTO>> getActivitatsByUsuari(@PathVariable Long idUsuari) {
        return ResponseEntity.status(HttpStatus.OK).body(activitatService.getActivitatsByUsuari(idUsuari));
    }

    @Autowired
    com.agenda.itic.repository.UsuariRepository usuariRepository;

    @PostMapping("/activitat")
    public ResponseEntity<ActivitatResponseDTO> createActivitat(@Valid @RequestBody ActivitatRequestDTO activitatRequestDTO) {
        if (activitatRequestDTO.getId_usuari() == null) {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            boolean resolved = false;
            if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof String) {
                String email = (String) auth.getPrincipal();
                if (email != null && !email.equals("anonymousUser")) {
                    java.util.Optional<com.agenda.itic.model.Usuari> optUsuari = usuariRepository.findByEmail(email);
                    if (optUsuari.isPresent()) {
                        activitatRequestDTO.setId_usuari(optUsuari.get().getId());
                        resolved = true;
                    }
                }
            }
            // Fallback: use the first user in the database
            if (!resolved) {
                java.util.List<com.agenda.itic.model.Usuari> allUsers = usuariRepository.findAll();
                if (!allUsers.isEmpty()) {
                    activitatRequestDTO.setId_usuari(allUsers.get(0).getId());
                } else {
                    throw new com.agenda.itic.exception.ResourceNotFoundException("No hi ha cap usuari registrat. Crea un usuari primer.");
                }
            }
        }
        ActivitatResponseDTO response = activitatService.createActivitat(activitatRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/activitat/{id}")
    public ResponseEntity<Void> deleteActivitat(@PathVariable Long id) {
        activitatService.deleteActivitat(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/activitats")
    public ResponseEntity<Void> deleteAllActivitats() {
        activitatService.getActivitatModel().forEach(a -> activitatService.deleteActivitat(a.getId_activitat()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
