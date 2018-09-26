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

/**
 * Rest controller para categorias
 * 
 * @author rodrigo
 *
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * retorna sempre status 200, mesmo com coleção vazia
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Categoria> categorias = categoriaService.listar();
		return ResponseEntity.ok(categorias);
	}

	/**
	 * retorna entidade pelo código
	 * 
	 * @param codigo
	 * @return
	 */
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaService.buscarPeloCodigo(codigo);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}

	/**
	 * persiste categoria e retorna uri + entidade
	 * 
	 * @param categoria
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria saved = categoriaService.salvar(categoria);

		// publicar evento para o listener especificado p/ o tipo de evento disparar a regra definida neste
		// desta forma é possível centralizar e reaproveitar rotinas comuns entre as classes
		publisher.publishEvent(new RecursoCriadoEvent(this, response, saved.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		categoriaService.remover(codigo);
	}
}
