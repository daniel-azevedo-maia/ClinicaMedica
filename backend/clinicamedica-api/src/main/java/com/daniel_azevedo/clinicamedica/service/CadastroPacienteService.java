package com.daniel_azevedo.clinicamedica.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.daniel_azevedo.clinicamedica.exception.EntidadeEmUsoException;
import com.daniel_azevedo.clinicamedica.exception.EntidadeNaoEncontradaException;
import com.daniel_azevedo.clinicamedica.model.Paciente;
import com.daniel_azevedo.clinicamedica.repository.PacienteRepository;

@Service
public class CadastroPacienteService {
	
	@Autowired
	private PacienteRepository pacienteRepository;
	

	public Paciente salvar(Paciente paciente) {
		return pacienteRepository.save(paciente);
	}


	public void excluir(Long pacienteId) {
		try {
			pacienteRepository.deleteById(pacienteId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de médico com código %d", pacienteId));
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Médico de código %d não pode ser removida, pois está em uso", pacienteId));
			}
		
		
	}

}
