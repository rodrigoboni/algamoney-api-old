package com.algaworks.algamoney.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;
import com.algaworks.algamoney.api.repository.filter.Filter;
import com.algaworks.algamoney.api.service.ServiceInterface;

@Service
public class CategoriaService implements ServiceInterface<Categoria, Object> {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Categoria> listAll() {
		return categoriaRepository.findAll();
	}

	@Override
	public List<Categoria> list(Object filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Categoria findByCodigo(Long codigo) {
		return categoriaRepository.findOne(codigo);
	}

	public Categoria persist(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public void remove(Long codigo) {
		categoriaRepository.delete(codigo);
	}

	@Override
	public Categoria update(Long codigo, Categoria entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
