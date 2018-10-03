package com.algaworks.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Configuração do servidor de autorização
 * @author s2it_rboni
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
		.inMemory() // persistencia em memoria para os usuários
		.withClient("angular") // id do cliente
		.secret("@ngul@r0") // senha / segredo do cliente
		.scopes("read", "write") // escopos que o cliente tem acesso
		.authorizedGrantTypes("password", "refresh_token") // fluxo oauth onde cliente recebe user+pass e envia p/ receber token - e tb fluxo p/ renovar token
		.accessTokenValiditySeconds(20) // tempo de validade do token
		.refreshTokenValiditySeconds(3600*24); // validade do token renovado (24h)
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
		.tokenStore(tokenStore()) // os tokens gerados pelo auth magager são persistidos para consulta
		.accessTokenConverter(getAccessTokenConverter())
		.reuseRefreshTokens(false) // não permitir reaproveitamento de tokens, forçando renovação
		.authenticationManager(authManager);
	}

	@Bean
	public JwtAccessTokenConverter getAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("algaworks"); // chave de validação dos tokens jwt
		return jwtAccessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(getAccessTokenConverter());
	}
}
