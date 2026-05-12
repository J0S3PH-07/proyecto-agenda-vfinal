package com.agenda.itic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agenda.itic.dto.SalaRequest;
import com.agenda.itic.dto.SalaResponseDTO;
import com.agenda.itic.exception.ResourceNotFoundException;
import com.agenda.itic.model.Sala;
import com.agenda.itic.repository.SalaRepository;

@Service
public class SalaService {

    @Autowired
    SalaRepository salaRepository;

    private SalaResponseDTO toDto(Sala sala) {
        return new SalaResponseDTO(
                sala.getId(),
                sala.getNom(),
                sala.getUbicacio() != null ? sala.getUbicacio().name() : "N/A",
                sala.getDescripcio(),
                sala.isActiva(),
                sala.getColorHex());
    }

    public List<SalaResponseDTO> getAllSalas() {
        return salaRepository.findAll().stream().map(sala -> toDto(sala)).toList();
    }

    public SalaResponseDTO createSala(SalaRequest salaRequest) {
        Sala sala = new Sala();
        sala.setNom(salaRequest.getNom());
        sala.setUbicacio(salaRequest.getUbicacio());
        sala.setDescripcio(salaRequest.getDescripcio());
        return toDto(salaRepository.save(sala));
    }

    public SalaResponseDTO updateSala(Long id, SalaRequest salaRequest) {
        Sala sala = salaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sala no trobada"));
        sala.setNom(salaRequest.getNom());
        sala.setUbicacio(salaRequest.getUbicacio());
        sala.setDescripcio(salaRequest.getDescripcio());
        return toDto(salaRepository.save(sala));
    }

    public void deleteSala(Long id) {
    Sala sala = salaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Sala no trobada con id: " + id));
    salaRepository.delete(sala);
}
}