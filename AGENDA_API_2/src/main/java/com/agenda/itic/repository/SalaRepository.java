package com.agenda.itic.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.agenda.itic.model.Sala;

@Repository
public interface SalaRepository extends CrudRepository<Sala, Long> {

    List<Sala> findAll();

}