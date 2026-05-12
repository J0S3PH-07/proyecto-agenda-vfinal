package com.agenda.itic.service;

import java.util.List;
import java.util.Locale;

import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agenda.itic.dto.CorreoPermitidoRequestDto;
import com.agenda.itic.dto.CorreoPermitidoResponseDto;
import com.agenda.itic.exception.BadRequestException;
import com.agenda.itic.exception.ResourceNotFoundException;
import com.agenda.itic.model.CorreoPermitido;
import com.agenda.itic.repository.CorreoPermitidoRepository;

@Service
public class CorreoPermitidoService {

    @Autowired
    CorreoPermitidoRepository correopermitidorepository;

    private CorreoPermitidoResponseDto toDTO(CorreoPermitido correoPermitido) {
        return new CorreoPermitidoResponseDto(correoPermitido.getId(), correoPermitido.getCorreo());
    }

    public List<CorreoPermitidoResponseDto> getAllCorreosPermitidos() {
        return correopermitidorepository.findAll().stream()
                .map(correoPermitido -> toDTO(correoPermitido))
                .toList();
    }

    public CorreoPermitidoResponseDto getCorreoPermitido(String correoPermitido) {
        String normalizedEmail = normalizeEmail(correoPermitido);
        if (normalizedEmail == null) {
            throw new BadRequestException("Correo inválido");
        }
        CorreoPermitido correoPermitido2 = correopermitidorepository.findByCorreoIgnoreCase(normalizedEmail);
        if (correoPermitido2 == null) {
            throw new ResourceNotFoundException("Correo no encontrado: " + correoPermitido);
        }
        return toDTO(correoPermitido2);
    }


    public CorreoPermitidoResponseDto createCorreoPermitido(CorreoPermitidoRequestDto correoPermitido) {
        String normalizedEmail = normalizeEmail(correoPermitido.getCorreo());
        if (normalizedEmail == null) {
            throw new BadRequestException("Correo inválido");
        }

        if (correopermitidorepository.findByCorreoIgnoreCase(normalizedEmail) != null) {
            throw new BadRequestException("Correo ya registrado");
        }
        return toDTO(correopermitidorepository.save(new CorreoPermitido(normalizedEmail)));
    }

    public void deleteCorreoPermitido(Long id) {
        CorreoPermitido correoPermitido = correopermitidorepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Correo no encontrado con id: " + id)
        );
        correopermitidorepository.delete(correoPermitido);
    }

    private String normalizeEmail(String email) {
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return normalized.isEmpty() ? null : normalized;
    }

}