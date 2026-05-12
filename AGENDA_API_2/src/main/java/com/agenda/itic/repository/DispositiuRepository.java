package com.agenda.itic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agenda.itic.model.Dispositiu;

@Repository
public interface DispositiuRepository extends JpaRepository<Dispositiu, Long> {
}
