package com.agenda.itic.dto;

import com.agenda.itic.model.PisoSala;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SalaRequest {

    @NotBlank(message = "El nom de la sala no pot estar buit")
    private String nom;

    @NotNull(message = "La ubicació de la sala no pot ser null")
    private PisoSala ubicacio;
    private String descripcio;

    public SalaRequest() {
    }

    public SalaRequest(String nom, PisoSala ubicacio, String descripcio) {
        this.nom = nom;
        this.ubicacio = ubicacio;
        this.descripcio = descripcio;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public PisoSala getUbicacio() {
        return ubicacio;
    }

    public void setUbicacio(PisoSala ubicacio) {
        this.ubicacio = ubicacio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

}
