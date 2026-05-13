package com.agenda.itic.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agenda.itic.dto.ActivitatRequestDTO;
import com.agenda.itic.dto.ActivitatResponseDTO;
import com.agenda.itic.model.Activitat;
import com.agenda.itic.service.ActivitatService;
import com.agenda.itic.repository.UsuariRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/activitats")
@CrossOrigin(origins = "*")
public class ActivitatController {

    @Autowired
    ActivitatService activitatService;
    
    @Autowired
    UsuariRepository usuariRepository;

    @GetMapping("/model")
    public ResponseEntity<List<Activitat>> getActivitatModel() {
        return ResponseEntity.ok(activitatService.getActivitatModel());
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<ActivitatResponseDTO>> getAllActivitats() {
        return ResponseEntity.ok(activitatService.getAllActivitats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivitatResponseDTO> getActivitatById(@PathVariable Long id) {
        return ResponseEntity.ok(activitatService.getActivitatById(id));
    }

    @GetMapping("/usuari/{idUsuari}")
    public ResponseEntity<List<ActivitatResponseDTO>> getActivitatsByUsuari(@PathVariable Long idUsuari) {
        return ResponseEntity.ok(activitatService.getActivitatsByUsuari(idUsuari));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ActivitatResponseDTO> createActivitat(@Valid @RequestBody ActivitatRequestDTO activitatRequestDTO) {
        if (activitatRequestDTO.getId_usuari() == null) {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof String email && !email.equals("anonymousUser")) {
                usuariRepository.findByEmail(email).ifPresent(u -> activitatRequestDTO.setId_usuari(u.getId()));
            }
            if (activitatRequestDTO.getId_usuari() == null) {
                usuariRepository.findAll().stream().findFirst().ifPresentOrElse(
                    u -> activitatRequestDTO.setId_usuari(u.getId()),
                    () -> { throw new RuntimeException("Crea un usuari primer."); }
                );
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(activitatService.createActivitat(activitatRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivitat(@PathVariable Long id) {
        activitatService.deleteActivitat(id);
        return ResponseEntity.noContent().build();
    }
}
