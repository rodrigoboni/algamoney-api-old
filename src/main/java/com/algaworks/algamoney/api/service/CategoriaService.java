package com.algaworks.algamoney.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Categoria> listAll() {
		return categoriaRepository.findAll();
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
}
