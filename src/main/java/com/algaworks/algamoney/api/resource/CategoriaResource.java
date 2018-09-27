package com.algaworks.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public ResponseEntity<?> list() {
		List<Categoria> categorias = categoriaService.list();
		return ResponseEntity.ok(categorias);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> findByCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaService.findByCodigo(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Categoria> persist(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria saved = categoriaService.persist(categoria);

		// publicar evento para o listener especificado p/ o tipo de evento disparar a regra definida neste
		// desta forma é possível centralizar e reaproveitar rotinas comuns entre as classes
		publisher.publishEvent(new RecursoCriadoEvent(this, response, saved.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long codigo) {
		categoriaService.remove(codigo);
	}
}
