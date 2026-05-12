package com.agenda.itic.dto;

import java.util.List;

public class UsuariResponseDto {
    private Long id_usuari;
    private String nom;
    private String email;
    private String rol;
    private List<String> permisos;
    private String picture;

    public UsuariResponseDto() {
    }

    public UsuariResponseDto(Long id_usuari, String nom, String email, String rol, List<String> permisos, String picture) {
        this.id_usuari = id_usuari;
        this.nom = nom;
        this.email = email;
        this.rol = rol;
        this.permisos = permisos;
        this.picture = picture;
    }

    public Long getId_usuari() {
        return id_usuari;
    }

    public void setId_usuari(Long id_usuari) {
        this.id_usuari = id_usuari;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }
}
