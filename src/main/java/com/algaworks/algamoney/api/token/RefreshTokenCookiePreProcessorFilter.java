package com.algaworks.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filtro / pre processador dos requests p/ add header com token refresh recebido pelo cookie
 * @author s2it_rboni
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // define prioridade alta p/ processar antes dos demais componentes
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		// atuar apenas qdo for requisição para obter o refresh token
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI()) 
				&& "refresh_token".equals(req.getParameter("grant_type"))
				&& req.getCookies() != null) {
			
			// percorrer cookies e obter refresh token
			for(Cookie cookie: req.getCookies()) {
				if(cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue();
					
					//substituir request pelo wrapper que add refresh token no parameter map
					req = new MyServletRequestWrapper(req, refreshToken);
				}
			}
		}
		
		chain.doFilter(req, response);
	}

	@Override
	public void destroy() {
	}
	
	private class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		/**
		 * Obter o mapa original de parâmetros e adicionar refresh token
		 */
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] {refreshToken});
			map.setLocked(true);
			
			return map;
		}
		
	}
}
