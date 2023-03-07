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
import com.daniel_azevedo.clinicamedica.model.Medico;
import com.daniel_azevedo.clinicamedica.repository.MedicoRepository;
import com.daniel_azevedo.clinicamedica.service.CadastroMedicoService;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private MedicoRepository medicoRepository;

	@Autowired
	private CadastroMedicoService cadastroMedico;

	@GetMapping
	public List<Medico> listarMedicos() {
		return medicoRepository.findAll();
	}

	@GetMapping("/{medicoId}")
	public ResponseEntity<Medico> buscarMedico(@PathVariable Long medicoId) {
		Optional<Medico> medico = medicoRepository.findById(medicoId);

		if (medico.isPresent()) {
			return ResponseEntity.ok(medico.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Medico adicionar(@RequestBody Medico medico) {
		return cadastroMedico.salvar(medico);
	}

	@PutMapping("/{medicoId}")
	public ResponseEntity<Medico> atualizar(@PathVariable Long medicoId,
			@RequestBody Medico medico) {
		Optional<Medico> medicoAtual = medicoRepository.findById(medicoId);
		
		if (medicoAtual.isPresent()) {
			BeanUtils.copyProperties(medico, medicoAtual.get(), "id");
			
			Medico medicoSalvo = cadastroMedico.salvar(medicoAtual.get());
			return ResponseEntity.ok(medicoSalvo);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{medicoId}")
	public ResponseEntity<?> remover(@PathVariable Long medicoId) {
		try {
			cadastroMedico.excluir(medicoId);	
			return ResponseEntity.noContent().build();
			
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
			
		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(e.getMessage());
		}
	}

}
