package com.algaworks.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoney.api.model.Categoria;

/**
 * Repositório para entidade categoria - ver interface JpaRepository p/ métodos disponíveis
 * @author rodrigo
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
