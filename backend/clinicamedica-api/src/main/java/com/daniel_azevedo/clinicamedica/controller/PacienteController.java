package com.daniel_azevedo.clinicamedica.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.daniel_azevedo.clinicamedica.exception.EntidadeEmUsoException;
import com.daniel_azevedo.clinicamedica.exception.EntidadeNaoEncontradaException;
import com.daniel_azevedo.clinicamedica.model.Paciente;
import com.daniel_azevedo.clinicamedica.repository.PacienteRepository;
import com.daniel_azevedo.clinicamedica.service.CadastroPacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private CadastroPacienteService cadastroPaciente;

	@GetMapping
	public List<Paciente> listarPacientes() {
		return pacienteRepository.findAll();
	}

	@GetMapping("/{pacienteId}")
	public ResponseEntity<Paciente> buscarPaciente(@PathVariable Long pacienteId) {
		Optional<Paciente> paciente = pacienteRepository.findById(pacienteId);

		if (paciente.isPresent()) {
			return ResponseEntity.ok(paciente.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Paciente adicionar(@RequestBody Paciente paciente) {
		return cadastroPaciente.salvar(paciente);
	}

	@PutMapping("/{pacienteId}")
	public ResponseEntity<Paciente> atualizar(@PathVariable Long pacienteId, @RequestBody Paciente paciente) {
		Optional<Paciente> pacienteAtual = pacienteRepository.findById(pacienteId);

		if (pacienteAtual.isPresent()) {
			BeanUtils.copyProperties(paciente, pacienteAtual.get(), "id");

			Paciente pacienteSalvo = cadastroPaciente.salvar(pacienteAtual.get());
			return ResponseEntity.ok(pacienteSalvo);
		}
		return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{pacienteId}")
	public ResponseEntity<?> remover(@PathVariable Long pacienteId) {
		try {
			cadastroPaciente.excluir(pacienteId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}

}
