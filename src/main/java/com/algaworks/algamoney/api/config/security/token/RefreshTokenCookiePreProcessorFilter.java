package com.algaworks.algamoney.api.config.security.token;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Map;

/**
 * Filtro / pre processador dos requests p/ add header com token refresh recebido pelo cookie
 * <p>
 * Ao gerar o refresh_token o mesmo é retirado do response e definido em cookie
 * Neste filtro é feito o processo inverso, a partir do cookie definir conteudo do request p/ o spring ler o refresh_token
 * <p>
 * Quando for feito logout o cookie não existirá mais no request
 *
 * @author s2it_rboni
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
    HttpServletRequest req = (HttpServletRequest) request;
    
    // atuar apenas qdo for requisição para obter o refresh token
    if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
        && "refresh_token".equals(req.getParameter("grant_type"))
        && req.getCookies() != null) {
      
      // percorrer cookies e obter refresh token
      for (Cookie cookie : req.getCookies()) {
        // verificar o nome do cookie e seu conteúdo (qdo for feito logout será definido um cookie vazio com o mesmo nome)
        if (cookie.getName().equals("refreshToken") && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
          //substituir request pelo wrapper que add refresh token no parameter map
          req = new MyServletRequestWrapper(req, cookie.getValue());
        }
      }
    }
    
    // continuar a cadeia de filtros
    chain.doFilter(req, response);
  }
  
  @Override
  public void destroy() {
  }
  
  /**
   * Class para implementar httprequest, onde será adicionado o param refresh_token
   */
  private class MyServletRequestWrapper extends HttpServletRequestWrapper {
    
    private String refreshToken;
    
    public MyServletRequestWrapper(final HttpServletRequest request, final String refreshToken) {
      super(request);
      this.refreshToken = refreshToken;
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
      ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
      map.put("refresh_token", new String[]{refreshToken});
      map.setLocked(true); // default do parametermap original do httpservletrequest
      
      return map;
    }
  }
}
