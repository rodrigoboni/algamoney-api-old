package com.algaworks.algamoney.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	public Pessoa buscarPeloCodigo(Long codigo) {
		return pessoaRepository.findOne(codigo);
	}

	public Pessoa salvar(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}

	public void remover(Long codigo) {
		pessoaRepository.delete(codigo);
	}
	
	public Pessoa atualizar (Long codigo, Pessoa pessoa) {
		Pessoa saved = buscarPessoaPeloCodigo(codigo);
		
		BeanUtils.copyProperties(pessoa, saved, "codigo");
		pessoaRepository.save(saved);
		
		return saved;
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa saved = buscarPessoaPeloCodigo(codigo);
		saved.setAtivo(ativo);
		pessoaRepository.save(saved);
	}	
	
	public Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa saved = pessoaRepository.findOne(codigo);
		if(saved == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return saved;
	}
}
