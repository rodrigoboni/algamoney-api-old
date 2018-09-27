package com.algaworks.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private LancamentoService lancamentoService;
	
	@GetMapping
	public ResponseEntity<?> list(LancamentoFilter filter) {
		// recebe filtros pelo bean lancamentofilter
		List<Lancamento> lancamentos = lancamentoService.list(filter);
		
		return ResponseEntity.ok(lancamentos);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> findByCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoService.findByCodigo(codigo);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> persist(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento saved = lancamentoService.persist(lancamento);
		
		//publicar evento para o listener especificado p/ o tipo de evento disparar a regra definida neste
		//desta forma é possível centralizar e reaproveitar rotinas comuns entre as classes
		publisher.publishEvent(new RecursoCriadoEvent(this, response, saved.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}
}
