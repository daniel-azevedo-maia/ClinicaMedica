package com.daniel_azevedo.clinicamedica.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.daniel_azevedo.clinicamedica.exception.EntidadeEmUsoException;
import com.daniel_azevedo.clinicamedica.exception.EntidadeNaoEncontradaException;
import com.daniel_azevedo.clinicamedica.model.Medico;
import com.daniel_azevedo.clinicamedica.repository.MedicoRepository;

@Service
public class CadastroMedicoService {

	@Autowired
	private MedicoRepository medicoRepository;

	public Medico salvar(Medico medico) {
		return medicoRepository.save(medico);
	}

	public void excluir(Long medicoId) {
		try {
			medicoRepository.deleteById(medicoId);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de médico com código %d", medicoId));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Médico de código %d não pode ser removido, pois está em uso", medicoId));
		}
	}

}