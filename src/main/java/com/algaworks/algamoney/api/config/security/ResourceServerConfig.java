package com.algaworks.algamoney.api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * Configuração da segurança do servidor de recursos
 *
 * @author s2it_rboni
 */
@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	// recebe instância da implementação desta interface (lê usuários da base de dados)
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * Configura o autorizador de recursos p/ buscar dados de usuários no serviço injetado (persistido no bd)
	 *
	 * @param auth
	 *
	 * @throws Exception
	 */
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}

	/**
	 * retorna codificador de senhas
	 *
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Define as regra do autorizador de recursos http
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated() // urls devem ser autenticadas (add regras antes p/ urls
				// que não precisem de auth)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // sessões stateless
				.and().csrf().disable(); // desabilitar csrf (cross site request forgery)
	}

	/**
	 * configurar autorizador p/ ser stateless
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}

	/**
	 * Tratamento de expressões oauth2 aplicadas nos métodos para autorizar acessos
	 *
	 * @return
	 */
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}
