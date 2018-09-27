package com.algaworks.algamoney.api.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.filter.Filter;
import com.algaworks.algamoney.api.service.ServiceInterface;

@Service
public class PessoaService implements ServiceInterface<Pessoa, Object> {
	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Pessoa> listAll() {
		return pessoaRepository.findAll();
	}

	@Override
	public List<Pessoa> list(Object filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Pessoa findByCodigo(Long codigo) {
		return pessoaRepository.findOne(codigo);
	}

	public Pessoa persist(Pessoa entity) {
		return pessoaRepository.save(entity);
	}

	public void remove(Long codigo) {
		pessoaRepository.delete(codigo);
	}
	
	public Pessoa update(Long codigo, Pessoa entity) {
		Pessoa saved = findPessoaByCodigo(codigo);
		
		BeanUtils.copyProperties(entity, saved, "codigo");
		pessoaRepository.save(saved);
		
		return saved;
	}

	public void updatePropertyAtivo(Long codigo, Boolean ativo) {
		Pessoa saved = findPessoaByCodigo(codigo);
		saved.setAtivo(ativo);
		pessoaRepository.save(saved);
	}	
	
	public Pessoa findPessoaByCodigo(Long codigo) {
		Pessoa saved = pessoaRepository.findOne(codigo);
		if(saved == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return saved;
	}
}
