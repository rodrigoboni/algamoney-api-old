package com.algaworks.algamoney.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.algamoney.api.model.Usuario;

/**
 * Repositório para usuários
 * 
 * @author rodrigo
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// método injetado em tempo execução pelo spring data
	public Optional<Usuario> findByEmail(String email);

}
