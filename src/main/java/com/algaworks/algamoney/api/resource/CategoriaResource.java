package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
  
  @Autowired
  private CategoriaService categoriaService;
  
  @Autowired
  private ApplicationEventPublisher publisher;
  
  //@CrossOrigin() habilitar cors para um método específico
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
  public ResponseEntity<?> list() {
    List<Categoria> categorias = categoriaService.listAll();
    return ResponseEntity.ok(categorias);
  }
  
  @GetMapping("/{codigo}")
  @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
  public ResponseEntity<Categoria> findByCodigo(@PathVariable Long codigo) {
    Categoria categoria = categoriaService.findByCodigo(codigo);
    return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
  }
  
  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
  public ResponseEntity<Categoria> persist(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
    final Categoria saved = categoriaService.persist(categoria);
    
    // publicar evento para o listener especificado p/ o tipo de evento disparar a regra definida neste
    // desta forma é possível centralizar e reaproveitar rotinas comuns entre as classes
    publisher.publishEvent(new RecursoCriadoEvent(this, response, saved.getCodigo()));
    
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }
  
  @DeleteMapping("/{codigo}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA') and #oauth2.hasScope('write')")
  public void remove(@PathVariable Long codigo) {
    categoriaService.remove(codigo);
  }
}
