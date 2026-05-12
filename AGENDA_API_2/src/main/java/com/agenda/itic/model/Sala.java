package com.agenda.itic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Entity
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_sala;

    @Column(nullable = false, unique = true)
    private String nom;
    @Enumerated(EnumType.STRING)
    private PisoSala ubicacio;
    private String descripcio;
    private boolean activa = true;
    private LocalDateTime dataCreacio;
    private LocalDateTime dataModificacio;
    @Enumerated(EnumType.STRING)
    private Color color;
    private String colorHex;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
    private List<Activitat> activitats;

    public enum Color {
        ROJO("#E53935"),
        VERDE("#43A047"),
        AZUL("#1E88E5"),
        AMARILLO("#FDD835"),
        NARANJA("#FB8C00"),
        MORADO("#8E24AA"),
        CYAN("#00ACC1"),
        MAGENTA("#D81B60");

        private final String hex;

        Color(String hex) {
            this.hex = hex;
        }

        public String getHex() {
            return hex;
        }

        public static Color aleatorio() {
            Color[] colors = values();
            return colors[ThreadLocalRandom.current().nextInt(colors.length)];
        }
    }

    @PrePersist
    protected void onCreate() {
        dataCreacio = LocalDateTime.now();
        dataModificacio = LocalDateTime.now();
        if (color == null) {
            color = Color.aleatorio();
            colorHex = color.getHex();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataModificacio = LocalDateTime.now();
    }

    public Sala() {
    }

    public Sala(Long id_sala, String nom, PisoSala ubicacio, String descripcio, Color color) {
        this.id_sala = id_sala;
        this.nom = nom;
        this.ubicacio = ubicacio;
        this.descripcio = descripcio;
        this.color = color;
        this.colorHex = color.getHex();
    }

    public Long getId() {
        return id_sala;
    }

    public void setId(Long id_sala) {
        this.id_sala = id_sala;
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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(LocalDateTime dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public LocalDateTime getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(LocalDateTime dataModificacio) {
        this.dataModificacio = dataModificacio;
    }

    public Color getColor() {
        return color;
    }   

    public void setColor(Color color) {
        this.color = color;
        this.colorHex = color.getHex();
    }

    public String getColorHex() {
        return colorHex;
    }
}