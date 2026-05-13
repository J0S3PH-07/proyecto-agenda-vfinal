package com.agenda.itic.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.itic.service.CorreoPermitidoService;

import jakarta.validation.Valid;

import com.agenda.itic.dto.CorreoPermitidoRequestDto;
import com.agenda.itic.dto.CorreoPermitidoResponseDto;

@RestController
@RequestMapping("/correos-permitidos")
public class CorreoPermitidoController {

    @Autowired
    CorreoPermitidoService correoPermitidoService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<CorreoPermitidoResponseDto>> getAllCorreosPermitidos() {
        return ResponseEntity.ok(correoPermitidoService.getAllCorreosPermitidos());
    }

    @GetMapping("/{email}")
    public ResponseEntity<CorreoPermitidoResponseDto> getCorreoPermitido(@PathVariable String email) {
        return ResponseEntity.ok(correoPermitidoService.getCorreoPermitido(email));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<CorreoPermitidoResponseDto> createCorreoPermitido(@Valid @RequestBody CorreoPermitidoRequestDto correoPermitido) {
        CorreoPermitidoResponseDto createdCorreoPermitido = correoPermitidoService.createCorreoPermitido(correoPermitido);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCorreoPermitido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorreoPermitido(@PathVariable Long id) {
        correoPermitidoService.deleteCorreoPermitido(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}