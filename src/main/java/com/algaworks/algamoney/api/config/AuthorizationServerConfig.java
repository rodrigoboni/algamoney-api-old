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
 * <p>
 * esta parte poderia estar separada em outro app / projeto p/ responder a outros micro-serviços por exemplo
 *
 * @author s2it_rboni
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
  
  @Autowired
  // recebe instância do provedor de autenticação default do spring
  private AuthenticationManager authManager;
  
  /**
   * Configura o autorizador de clientes oauth
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory() // persistencia em memoria para os usuários
        .withClient("angular") // id do cliente
        .secret("@ngul@r0") // senha / segredo do cliente
        .scopes("read", "write") // escopos que o cliente tem acesso - além da permissão por usuário pode ser especificado permissão por cliente
        .authorizedGrantTypes("password", "refresh_token") // fluxo oauth onde cliente recebe user+pass e envia p/ receber token - e tb fluxo p/ renovar token
        .accessTokenValiditySeconds(120) // tempo de validade do token (2min)
        .refreshTokenValiditySeconds(3600) // validade do token de renovação (1h)
        .and()
        .withClient("mobile") // id do cliente - exemplo de segundo cliente
        .secret("m0b1l30") // senha / segredo do cliente
        .scopes("read") // este cliente tem escopo de leitura somente
        .authorizedGrantTypes("password", "refresh_token") // fluxo oauth onde cliente recebe user+pass e envia p/ receber token - e tb fluxo p/ renovar token
        .accessTokenValiditySeconds(120) // tempo de validade do token (2min)
        .refreshTokenValiditySeconds(3600); // validade do token de renovação (1h)
  }
  
  /**
   * configura o autorizador de acesso dos endpoints
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(tokenStore()) // os tokens gerados pelo auth magager são persistidos para consulta
        .accessTokenConverter(getAccessTokenConverter())
        .reuseRefreshTokens(false) // não permitir reaproveitamento de tokens de renovação, forçando geração de novo token
        .authenticationManager(authManager);
  }
  
  /**
   * Retorna helper p/ converter request oauth em jwt e vice-versa
   *
   * @return
   */
  @Bean
  public JwtAccessTokenConverter getAccessTokenConverter() {
    final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setSigningKey("algaworks"); // chave de validação dos tokens jwt //TODO DEFINIR CHAVE MAIS FORTE
    return jwtAccessTokenConverter;
  }
  
  /**
   * retorna instância do store de tokens jwt
   * <p>
   * utilizado para validar apenas, não está armazenando os tokens
   * ver métodos na classe jwttokenstore para persistir e remover tokens
   *
   * @return
   */
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(getAccessTokenConverter());
  }
}
