package com.agenda.itic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agenda.itic.model.Activitat;
import java.util.List;

@Repository
public interface ActivitatRepository extends JpaRepository<Activitat, Long> {
    List<Activitat> findByUserId(Long idUsuari);
}
