package com.algaworks.algamoney.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Usuario;
import com.algaworks.algamoney.api.repository.UsuarioRepository;

/**
 * Implementação do serviço de detalhes dos usuários (base em bd)
 * @author rodrigo
 *
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Localizar e carregar dados do usuário
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//buscar usuario pelo email
		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		
		//se o optional não retornar nada dispara exception para indicar que usuário não existe
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha inválido"));
		
		//retorna user com login, senha e permissoes
		return new User(email, usuario.getSenha(), getPermissoes(usuario));
	}

	/**
	 * Obter permissoes do usuário
	 * @param usuario
	 * @return
	 */
	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> permissoes = new HashSet<>();
		
		//percorre permissões do usuário e monta lista desta p/ retornar no usuário
		usuario.getPermissao().forEach(p -> permissoes.add(new SimpleGrantedAuthority(p.getDescricao())));
		
		return permissoes;
	}
}
