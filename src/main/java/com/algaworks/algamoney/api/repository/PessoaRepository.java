package com.algaworks.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoney.api.model.Pessoa;

/**
 * Repositório para entidade pessoa - ver interface JpaRepository p/ métodos disponíveis
 * @author rodrigo
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
