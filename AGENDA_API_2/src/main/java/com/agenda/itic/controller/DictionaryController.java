package com.agenda.itic.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.itic.dto.RolDictionaryResponseDto;
import com.agenda.itic.model.PisoSala;
import com.agenda.itic.model.Rol;

@RestController
@RequestMapping("/dictionary")
@CrossOrigin(origins = "*")
public class DictionaryController {

    @GetMapping("/roles")
    public ResponseEntity<List<RolDictionaryResponseDto>> getRolesDictionary() {
        List<RolDictionaryResponseDto> roles = Arrays.stream(Rol.values())
                .map(rol -> new RolDictionaryResponseDto(
                        rol.name(),
                        rol.getPermisos().stream().sorted().toList()))
                .toList();

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/pisos-sala")
    public ResponseEntity<List<String>> getPisosSalaDictionary() {
        List<String> pisos = Arrays.stream(PisoSala.values())
                .map(Enum::name)
                .toList();

        return ResponseEntity.ok(pisos);
    }
}