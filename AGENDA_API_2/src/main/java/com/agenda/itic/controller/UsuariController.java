package com.agenda.itic.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.itic.dto.UsuariResponseDto;
import com.agenda.itic.dto.UsuariTokenDto;
import com.agenda.itic.service.UsuariService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/usuaris")
@CrossOrigin(origins = "*")
public class UsuariController {

    @Autowired
    UsuariService usuariService;

    @GetMapping("/usuaris")
    public ResponseEntity<List<UsuariResponseDto>> getUsuaris() {
        List<UsuariResponseDto> usuaris = usuariService.getUsuaris();
        return ResponseEntity.status(HttpStatus.OK).body(usuaris);
    }

    @GetMapping("/usuaris/{actiu}")
    public ResponseEntity<List<UsuariResponseDto>> getUsuarisActius(@PathVariable boolean actiu) {
        List<UsuariResponseDto> usuaris = usuariService.getUsuarisActius(actiu);
        return ResponseEntity.status(HttpStatus.OK).body(usuaris);
    }

    @PostMapping("/usuaris")
    public ResponseEntity<UsuariResponseDto> createUsuari(@Valid @RequestBody UsuariTokenDto usuari) {
        UsuariResponseDto createdUsuari = usuariService.createUsuari(usuari);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuari);
    }

    @PutMapping("/usuaris/{id}")
    public ResponseEntity<UsuariResponseDto> updateUsuari(@PathVariable Long id, @Valid @RequestBody UsuariTokenDto usuariRequestDTO) {
        UsuariResponseDto updatedUsuari = usuariService.updateUsuari(id, usuariRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUsuari);
    }

    @DeleteMapping("/usuaris/{id}")
    public ResponseEntity<Void> deleteUsuari(@PathVariable Long id) {
        usuariService.deleteUsuari(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/token")
    public ResponseEntity<UsuariResponseDto> createOrUpdateUsuariFromToken(@RequestHeader("Authorization") String authHeader) {
        UsuariResponseDto usuari = usuariService.createOrUpdateUsuariFromToken(authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuari);
    }
    
    @GetMapping("/usuaris/profes")
    public ResponseEntity<List<UsuariResponseDto>> getUsuariProfes() {
        return ResponseEntity.ok(usuariService.getUsuarisProfes());
    }
    
}
