package com.agenda.itic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Dispositiu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_dispositiu;
    private String nom;
    private String ip;
    private String mac;
    private String model;
    private String tipus;
    private String marca;
    private String numero_serie;
    private String sala;
    private boolean actiu;
    private LocalDateTime dataCreacio;
    private LocalDateTime dataModificacio;

    @PrePersist
    protected void onCreate() {
        dataCreacio = LocalDateTime.now();
        dataModificacio = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataModificacio = LocalDateTime.now();
    }

    public Dispositiu() {
    }

    public Dispositiu(Long id_dispositiu, String nom, String ip, String mac, String model, String tipus,
            String marca, String numero_serie, String sala, boolean actiu) {
        this.id_dispositiu = id_dispositiu;
        this.nom = nom;
        this.ip = ip;
        this.mac = mac;
        this.model = model;
        this.tipus = tipus;
        this.marca = marca;
        this.numero_serie = numero_serie;
        this.sala = sala;
        this.actiu = actiu;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Long getId_dispositiu() {
        return id_dispositiu;
    }

    public void setId_dispositiu(Long id_dispositiu) {
        this.id_dispositiu = id_dispositiu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean getActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
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
}
