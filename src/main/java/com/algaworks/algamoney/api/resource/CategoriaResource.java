package com.algaworks.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;

/**
 * Rest controller para categorias
 * @author rodrigo
 *
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/**
	 * retorna sempre status 200, mesmo com coleção vazia
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Categoria> categorias = categoriaRepository.findAll();
		return ResponseEntity.ok(categorias);
	}

	/**
	 * persiste categoria e retorna uri + entidade
	 * @param categoria
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria) {
		Categoria saved = categoriaRepository.save(categoria);

		// montar uri para obter o registro persistido
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(saved.getCodigo()).toUri();
		
		return ResponseEntity.created(uri).body(saved);
	}

	/**
	 * retorna entidade pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		return categoria != null ? ResponseEntity.ok(categoria): ResponseEntity.notFound().build();
	}
}
