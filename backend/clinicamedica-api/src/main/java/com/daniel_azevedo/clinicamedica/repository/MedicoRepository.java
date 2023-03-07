package com.daniel_azevedo.clinicamedica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniel_azevedo.clinicamedica.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

}
