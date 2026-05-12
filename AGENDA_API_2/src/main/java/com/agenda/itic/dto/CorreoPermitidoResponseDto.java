package com.agenda.itic.dto;

public class CorreoPermitidoResponseDto {
    private Long id;
    private String correo;

    public CorreoPermitidoResponseDto() {}

    public CorreoPermitidoResponseDto(Long id, String correo) {
        this.id = id;
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
