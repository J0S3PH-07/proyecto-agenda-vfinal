package com.agenda.itic.dto;
import java.time.LocalDate;
import java.time.LocalTime;


public class ActivitatResponseDTO {
    private Long id_activitat;
    private Long id_sala;
    private String nom_sala;
    private Long id_usuari;
    private String nom_usuari;
    private String titol;
    private String resum;
    private String descripcio;
    private LocalDate data;
    private LocalTime horaInici;
    private LocalTime horaFi;
    private String estat;
    private boolean activa;


    
    public ActivitatResponseDTO(Long id_activitat, Long id_sala, String nom_sala, Long id_usuari, String nom_usuari, String titol, String resum, String descripcio,
            LocalDate data, LocalTime horaInici, LocalTime horaFi, String estat, boolean activa) {
        this.id_activitat = id_activitat;
        this.id_sala = id_sala;
        this.nom_sala = nom_sala;
        this.id_usuari = id_usuari;
        this.nom_usuari = nom_usuari;
        this.titol = titol;
        this.resum = resum;
        this.descripcio = descripcio;
        this.data = data;
        this.horaInici = horaInici;
        this.horaFi = horaFi;
        this.estat = estat;
        this.activa = activa;
    }

    public Long getId_activitat() {
        return id_activitat;
    }


    public Long getId_sala() {
        return id_sala;
    }

    public String getNom_sala() {
        return nom_sala;
    }

    public void setNom_sala(String nom_sala) {
        this.nom_sala = nom_sala;
    }


    public void setId_sala(Long id_sala) {
        this.id_sala = id_sala;
    }

    public Long getId_usuari() {
        return id_usuari;
    }

    public void setId_usuari(Long id_usuari) {
        this.id_usuari = id_usuari;
    }

    public String getNom_usuari() {
        return nom_usuari;
    }

    public void setNom_usuari(String nom_usuari) {
        this.nom_usuari = nom_usuari;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getDescripcio() {
        return descripcio;
    }



    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }



    public LocalDate getData() {
        return data;
    }



    public void setData(LocalDate data) {
        this.data = data;
    }



    public LocalTime getHoraInici() {
        return horaInici;
    }



    public void setHoraInici(LocalTime horaInici) {
        this.horaInici = horaInici;
    }



    public LocalTime getHoraFi() {
        return horaFi;
    }



    public void setHoraFi(LocalTime horaFi) {
        this.horaFi = horaFi;
    }



    public boolean isActiva() {
        return activa;
    }



    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getResum() {
        return resum;
    }

    public void setResum(String resum) {
        this.resum = resum;
    }

    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }
}
