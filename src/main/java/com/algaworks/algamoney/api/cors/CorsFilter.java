package com.algaworks.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filtro para implementar CORS
 * @author s2it_rboni
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	// definir como * para permitir qq origin
	//private final String allowedOrigin = "http://localhost:8000"; //TODO - Melhorar lendo a partir de arquivo properties
	private final String allowedOrigin = "*";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		// sempre add header indicando origin permitida e envio de credenciais (p/ browser enviar cookies)
		response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		// tratar request options p/ origin válida
		if("OPTIONS".equals(request.getMethod()) && isValidOrigin(request.getHeader("Origin"))) {
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
		if("*".equals(allowedOrigin)) {
			return true;
		}
		
		if(origin == null || origin.isEmpty()) {
			return false;
		}
		
		return origin.equalsIgnoreCase(allowedOrigin);
	}
}
