package com.agenda.itic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.itic.dto.SalaRequest;
import com.agenda.itic.dto.SalaResponseDTO;
import com.agenda.itic.service.SalaService;

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
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/sala")
@CrossOrigin(origins = "*")
public class SalaController {

    @Autowired
    SalaService salaService;

    @GetMapping("/salas")
    public ResponseEntity<List<SalaResponseDTO>> getAllSalas() {
        List<SalaResponseDTO> salas = salaService.getAllSalas();
        return ResponseEntity.status(HttpStatus.OK).body(salas);
    }

    @PostMapping("/salas")
    public ResponseEntity<SalaResponseDTO> postSala(@Valid @RequestBody SalaRequest salaRequest) {
        SalaResponseDTO sala = salaService.createSala(salaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sala);
    }

    @PutMapping("/salas/{id}")
    public ResponseEntity<SalaResponseDTO> putSala(@PathVariable Long id, @Valid @RequestBody SalaRequest salaRequest) {
        SalaResponseDTO salaActualitzada = salaService.updateSala(id, salaRequest);
        return ResponseEntity.status(HttpStatus.OK).body(salaActualitzada);
        
    }

    @DeleteMapping("/salas/{id}")
    public ResponseEntity<Void> deleteSala(@PathVariable Long id) {
        salaService.deleteSala(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}