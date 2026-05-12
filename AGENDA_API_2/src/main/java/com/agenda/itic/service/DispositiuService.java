package com.agenda.itic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agenda.itic.dto.DispositiuRequestDTO;
import com.agenda.itic.exception.BadRequestException;
import com.agenda.itic.exception.ResourceNotFoundException;
import com.agenda.itic.model.Dispositiu;
import com.agenda.itic.repository.DispositiuRepository;
import com.agenda.itic.repository.UsuariRepository;

@Service
public class DispositiuService {

    private final UsuariRepository usuariRepository;

    @Autowired
    DispositiuRepository dispositiuRepository;

    DispositiuService(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    public Dispositiu mapToDispositiu(DispositiuRequestDTO dispositiuDTO) {
        Dispositiu dispositiu = new Dispositiu();
        dispositiu.setNom(dispositiuDTO.getNom());
        dispositiu.setTipus(dispositiuDTO.getTipus());
        dispositiu.setMarca(dispositiuDTO.getMarca());
        dispositiu.setModel(dispositiuDTO.getModel());
        dispositiu.setNumero_serie(dispositiuDTO.getNumero_serie());
        dispositiu.setSala(dispositiuDTO.getSala());
        dispositiu.setActiu(dispositiuDTO.getActiu());
        return dispositiu;
    }

    public List<Dispositiu> getDispositius() {
        return dispositiuRepository.findAll();
    }

    public Dispositiu createDispositiu(DispositiuRequestDTO dispositiu) {
        if (dispositiu == null) {
            throw new BadRequestException("DispositiuRequestDTO no pot ser null");
        }
        return dispositiuRepository.save(mapToDispositiu(dispositiu));
        
    }

    public Dispositiu updateDispositiu(Long id, DispositiuRequestDTO dispositiuDTO) {
        if (dispositiuDTO == null) {
            throw new BadRequestException("DispositiuRequestDTO no pot ser null");
        }
        Dispositiu existingDispositiu = dispositiuRepository.findById(id).orElse(null);
        if (existingDispositiu == null) {
            throw new ResourceNotFoundException("Dispositiu no trobat");
        }
        existingDispositiu = mapToDispositiu(dispositiuDTO);
        existingDispositiu.setId_dispositiu(id);
        return dispositiuRepository.save(existingDispositiu);
    }

    public void deleteDispositiu(Long id) {
        if (usuariRepository.existsById(id)) {
            throw new ResourceNotFoundException("No es pot eliminar el dispositiu perquè està associat a un usuari");
        }
        
        dispositiuRepository.deleteById(id);
    }
}
