package com.algaworks.algamoney.api.config.security.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.algaworks.algamoney.api.config.property.ApplicationProperties;

/**
 * Modificar response http para remover refresh_token do body e por em cookie Este filtro será acionado somente qdo for responder a
 * request para obter novo token, a partir do refresh_token
 * <p>
 * Define cookie seguro (qdo em https) - desta forma o js não consegue acessar o cookie, gerando risco de segurança Uma vez definido
 * o cookie, este será enviado em todos os requests, devido ao path definido no cookie ser o mesmo dos requests seguintes
 *
 * @author s2it_rboni
 */
@ControllerAdvice
// OAuth2AccessToken é o tipo de retorno do método utilizado pelo método que trata da validação dos tokens
// (org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken)
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Autowired
	private ApplicationProperties algamoneyApiProperty;
	
	/**
	 * Qdo este método retornar true o método beforebodywrite será invocado(filtra qdo o body deve ser modificado)
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// interceptar response somente qdo for request do oauth
		// definido em
		// org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	/**
	 * Definir cookie e remover o token do response body
	 */
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		addRefreshTokenCookie(body.getRefreshToken().getValue(), ((ServletServerHttpRequest) request).getServletRequest(),
				((ServletServerHttpResponse) response).getServletResponse());

		removeRefreshTokenBody((DefaultOAuth2AccessToken) body);

		return body;
	}

	private void addRefreshTokenCookie(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true); // impede acesso via js - gerenciado pelo browser apenas
		cookie.setSecure(algamoneyApiProperty.getSecurity().isEnableHttps());
		cookie.setPath(request.getContextPath() + "/oauth/token");
		cookie.setMaxAge(2592000); // 30 dias
		response.addCookie(cookie);
	}

	private void removeRefreshTokenBody(DefaultOAuth2AccessToken body) {
		body.setRefreshToken(null);
	}
}
