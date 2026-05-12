package com.agenda.itic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agenda.itic.model.CorreoPermitido;

@Repository
public interface CorreoPermitidoRepository extends JpaRepository<CorreoPermitido, Long> {
    
    CorreoPermitido findByCorreo(String correo);

    CorreoPermitido findByCorreoIgnoreCase(String correo);
}