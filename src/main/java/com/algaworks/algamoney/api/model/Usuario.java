package com.algaworks.algamoney.api.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="usuario")
public class Usuario {
	private Long codigo;
	private String email;
	private String senha;
	private List<Permissao> permissao;
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public List<Permissao> getPermissao() {
		return permissao;
	}
	public void setPermissao(List<Permissao> permissao) {
		this.permissao = permissao;
	}
	
	
}
