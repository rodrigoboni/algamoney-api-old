package com.algaworks.algamoney.api.event.listener;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Listener para evento da aplicação
 * (permitir reaproveitamento de regras / rotinas comuns após persistir dados, validações etc)
 *
 * @author rodrigo.boni
 */
@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {
  
  @Override
  public void onApplicationEvent(RecursoCriadoEvent event) {
    addHeaderLocation(event.getResponse(), event.getCodigo());
  }
  
  private void addHeaderLocation(HttpServletResponse response, Long codigo) {
    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{codigo}")
        .buildAndExpand(codigo)
        .toUri();
    
    response.setHeader("Location", uri.toASCIIString());
  }
}
