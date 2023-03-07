package com.daniel_azevedo.clinicamedica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniel_azevedo.clinicamedica.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
