package com.agenda.itic.dto;

import java.util.List;

public class RolDictionaryResponseDto {

    private String rol;
    private List<String> permisos;

    public RolDictionaryResponseDto(String rol, List<String> permisos) {
        this.rol = rol;
        this.permisos = permisos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }
}