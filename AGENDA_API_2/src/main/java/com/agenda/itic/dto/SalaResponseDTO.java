package com.agenda.itic.dto;

public class SalaResponseDTO {
    private Long id_sala;
    private String nom;
    private String ubicacio;
    private String descripcio;
    private boolean activa;
    private String colorHex;

    public SalaResponseDTO(Long id_sala, String nom, String ubicacio, String descripcio, boolean activa,
            String colorHex) {
        this.id_sala = id_sala;
        this.nom = nom;
        this.ubicacio = ubicacio;
        this.descripcio = descripcio;
        this.activa = activa;
        this.colorHex = colorHex;
    }

    public Long getId_sala() {
        return id_sala;
    }

    public String getNom() {
        return nom;
    }

    public String getUbicacio() {
        return ubicacio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public boolean isActiva() {
        return activa;
    }

    public String getColorHex() {
        return colorHex;
    }
}
