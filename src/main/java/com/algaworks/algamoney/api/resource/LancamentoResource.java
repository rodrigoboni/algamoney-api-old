package com.algaworks.algamoney.api.resource;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.projection.ResumoLancamento;
import com.algaworks.algamoney.api.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private LancamentoService lancamentoService;

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<?> list(LancamentoFilter filter, Pageable pageable) {
		// recebe filtros pelo bean lancamentofilter
		List<Lancamento> lancamentos = lancamentoService.list(filter, pageable);
		Long totalRecords = lancamentoService.getListTotalRecords(filter);

		return ResponseEntity.ok(new PageImpl<>(lancamentos, pageable, totalRecords));
	}

	/**
	 * Ilustrar meio para retornar dados de forma resumida (projeção)
	 *
	 * @param filter
	 * @param pageable
	 *
	 * @return
	 */
	@GetMapping(params = "resumo") // http://..../lancamentos?resumo
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<?> quickList(LancamentoFilter filter, Pageable pageable) {
		// recebe filtros pelo bean lancamentofilter
		List<ResumoLancamento> lancamentos = lancamentoService.quickList(filter, pageable);
		Long totalRecords = lancamentoService.getListTotalRecords(filter);

		return ResponseEntity.ok(new PageImpl<>(lancamentos, pageable, totalRecords));
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> findByCodigo(@PathVariable Long codigo) {
		Lancamento lancamento = lancamentoService.findByCodigo(codigo);
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> persist(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento saved = lancamentoService.persist(lancamento);

		// publicar evento para o listener especificado p/ o tipo de evento disparar a regra definida neste
		// desta forma é possível centralizar e reaproveitar rotinas comuns entre as classes
		publisher.publishEvent(new RecursoCriadoEvent(this, response, saved.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	public void remove(@PathVariable Long codigo) {
		lancamentoService.remove(codigo);
	}
}
