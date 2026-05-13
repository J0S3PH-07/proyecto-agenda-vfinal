package com.agenda.itic.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agenda.itic.dto.UsuariResponseDto;
import com.agenda.itic.dto.UsuariTokenDto;
import com.agenda.itic.service.UsuariService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuaris")
@CrossOrigin(origins = "*")
public class UsuariController {

    @Autowired
    UsuariService usuariService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<UsuariResponseDto>> getUsuaris() {
        return ResponseEntity.ok(usuariService.getUsuaris());
    }

    @GetMapping("/{actiu}")
    public ResponseEntity<List<UsuariResponseDto>> getUsuarisActius(@PathVariable boolean actiu) {
        return ResponseEntity.ok(usuariService.getUsuarisActius(actiu));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<UsuariResponseDto> createUsuari(@Valid @RequestBody UsuariTokenDto usuari) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariService.createUsuari(usuari));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuariResponseDto> updateUsuari(@PathVariable Long id, @Valid @RequestBody UsuariTokenDto usuariRequestDTO) {
        return ResponseEntity.ok(usuariService.updateUsuari(id, usuariRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuari(@PathVariable Long id) {
        usuariService.deleteUsuari(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/token")
    public ResponseEntity<UsuariResponseDto> createOrUpdateUsuariFromToken(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariService.createOrUpdateUsuariFromToken(authHeader));
    }
    
    @GetMapping("/profes")
    public ResponseEntity<List<UsuariResponseDto>> getUsuariProfes() {
        return ResponseEntity.ok(usuariService.getUsuarisProfes());
    }
}
