package com.algaworks.algamoney.api.config.security;

import java.util.Collection;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;

import com.algaworks.algamoney.api.model.Usuario;

public class UsuarioSistema extends User {

	private Usuario usuario;
	
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		//super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;
	}
}
