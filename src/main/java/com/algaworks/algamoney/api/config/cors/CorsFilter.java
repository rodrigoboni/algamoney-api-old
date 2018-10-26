package com.algaworks.algamoney.api.config.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.algaworks.algamoney.api.config.property.ApplicationProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro para implementar controle CORS (Cross Origin Resource Sharing) Não foi utilizado o método corsConfigurer do spring devido
 * a este não ter integração com oauth2
 *
 * @author s2it_rboni
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	@Autowired
	private ApplicationProperties appConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// sempre add header indicando origin permitida e envio de credenciais (p/
		// browser enviar cookies)
		// (informação para o browser lidar com cors)
		response.setHeader("Access-Control-Allow-Origin", appConfig.getSecurity().getAllowedOrigin());
		response.setHeader("Access-Control-Allow-Credentials", "true");

		// tratar request options p/ origin válida
		if ("OPTIONS".equals(request.getMethod()) && isValidOrigin(request.getHeader("Origin"))) {
			// retornar métodos permitidos para a origin
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			// retornar headers permitidos para a origin
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			// tempo de validade do request options (1h) - tempo p/ fazer novo req options
			response.setHeader("Access-Control-Max-Age", "3600");

			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			// não é options ou não é origin permitida - seguir cadeia de filtros
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}

	private boolean isValidOrigin(String origin) {
		if ("*".equals(appConfig.getSecurity().getAllowedOrigin())) {
			return true;
		}

		if (origin == null || origin.isEmpty()) {
			return false;
		}

		return origin.equalsIgnoreCase(appConfig.getSecurity().getAllowedOrigin());
	}
}
