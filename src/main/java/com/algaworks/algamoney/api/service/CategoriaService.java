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

	public List<Categoria> listar() {
		return categoriaRepository.findAll();
	}

	/**
	 * retorna entidade pelo código
	 * 
	 * @param codigo
	 * @return
	 */
	public Categoria buscarPeloCodigo(Long codigo) {
		return categoriaRepository.findOne(codigo);
	}

	/**
	 * persiste categoria e retorna uri + entidade
	 * 
	 * @param categoria
	 * @return
	 */
	public Categoria salvar(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public void remover(Long codigo) {
		categoriaRepository.delete(codigo);
	}
}
