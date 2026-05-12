package com.agenda.itic.service;

import com.agenda.itic.repository.UsuariRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agenda.itic.dto.ActivitatRequestDTO;
import com.agenda.itic.dto.ActivitatResponseDTO;
import com.agenda.itic.exception.ResourceNotFoundException;
import com.agenda.itic.model.Activitat;
import com.agenda.itic.repository.ActivitatRepository;
import com.agenda.itic.repository.SalaRepository;

@Service
public class ActivitatService {

    @Autowired
    UsuariRepository usuariRepository;

    @Autowired
    ActivitatRepository activitatRepository;

    @Autowired
    SalaRepository salaRepository;


    ActivitatService(UsuariRepository usuariRepository) {
    }


    public List<ActivitatResponseDTO> getAllActivitats() {
        return activitatRepository.findAll()
                .stream()
                .map(act -> toDTO(act))
                .toList();
    }

    private ActivitatResponseDTO toDTO(Activitat a) {
        return new ActivitatResponseDTO(
                a.getId_activitat(),
                a.getSala().getId(),
                a.getSala().getNom(),
                a.getUser().getId(),
                a.getUser().getNom(),
                a.getTitol(),
                a.getResum(),
                a.getDescripcio(),
                a.getData(),
                a.getHoraInici(),
                a.getHoraFi(),
                a.getEstat() != null ? a.getEstat().name() : "programada",
                a.isActiva());
    }

    private Activitat toModel(ActivitatRequestDTO activitatRequestDTO) {
        Activitat activitat = new Activitat();
        activitat.setSala(salaRepository.findById(activitatRequestDTO.getId_sala()).orElseThrow(() -> new ResourceNotFoundException("Sala no trobada")));
        activitat.setTitol(activitatRequestDTO.getTitol());
        activitat.setResum(activitatRequestDTO.getResum());
        activitat.setDescripcio(activitatRequestDTO.getDescripcio());
        activitat.setData(activitatRequestDTO.getData());
        activitat.setHoraInici(activitatRequestDTO.getHoraInici());
        activitat.setHoraFi(activitatRequestDTO.getHoraFi());
        activitat.setEstat(activitatRequestDTO.getEstat() != null ? Activitat.Estat.valueOf(activitatRequestDTO.getEstat()) : Activitat.Estat.programada);
        activitat.setUser(usuariRepository.findById(activitatRequestDTO.getId_usuari()).orElseThrow(() -> new ResourceNotFoundException("Usuari no trobat")));
        activitat.setActiva(activitatRequestDTO.getVisible() != null ? activitatRequestDTO.getVisible() : true);
        return activitat;
    }

    public ActivitatResponseDTO getActivitatById(Long id) {
        return toDTO(activitatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activitat no trobada")));
    }

    public List<ActivitatResponseDTO> getActivitatsByUsuari(Long idUsuari) {
        return activitatRepository.findByUserId(idUsuari)
                .stream()
                .map(act -> toDTO(act))
                .toList();
    }

    public ActivitatResponseDTO createActivitat(ActivitatRequestDTO activitatRequestDTO) {
        if (!salaRepository.existsById(activitatRequestDTO.getId_sala())) {
            throw new ResourceNotFoundException("No se puede crear la actividad: La sala con ID " 
            + activitatRequestDTO.getId_sala() + " no existe.");
        }
        if (!usuariRepository.existsById(activitatRequestDTO.getId_usuari())) {
            throw new ResourceNotFoundException("No se puede crear la actividad: El usuario con ID " 
            + activitatRequestDTO.getId_usuari() + " no existe.");
        }
        if (activitatRequestDTO.getHoraInici().isAfter(activitatRequestDTO.getHoraFi())) {
            throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la hora de fin.");
        }
        if (activitatRequestDTO.getHoraInici().equals(activitatRequestDTO.getHoraFi())) {
            throw new IllegalArgumentException("La hora de inicio no puede ser igual a la hora de fin.");
        }
        if (activitatRequestDTO.getData().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de la actividad no puede ser anterior a la fecha actual.");
        }
        if (activitatRequestDTO.getData().isEqual(java.time.LocalDate.now()) && activitatRequestDTO.getHoraInici().isBefore(java.time.LocalTime.now())) {
            throw new IllegalArgumentException("La hora de inicio de la actividad no puede ser anterior a la hora actual.");
        }
        Activitat activitat = toModel(activitatRequestDTO);
        activitat = activitatRepository.save(activitat);
        return toDTO(activitat);
    }

    public void deleteActivitat(Long id) {
        Activitat activitat = activitatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activitat no trobada"));
        activitatRepository.delete(activitat);
    }

    public List<Activitat> getActivitatModel() {
        return activitatRepository.findAll();
    }
}