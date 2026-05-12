package com.agenda.itic.dto;

import jakarta.validation.constraints.NotBlank;

public class CorreoPermitidoRequestDto {
    @NotBlank(message = "El correo no puede ser vacío")
    private String correo;

    public CorreoPermitidoRequestDto() {}

    public CorreoPermitidoRequestDto(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
