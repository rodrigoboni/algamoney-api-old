package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.projection.ResumoLancamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoService {

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Lancamento> list(LancamentoFilter filter, Pageable pageable) {
		return lancamentoRepository.filter(filter, pageable);
	}

	public List<ResumoLancamento> quickList(LancamentoFilter filter, Pageable pageable) {
		return lancamentoRepository.Quickfilter(filter, pageable);
	}

	public Long getListTotalRecords(LancamentoFilter filter) {
		return lancamentoRepository.getFilterTotalRecords(filter);
	}

	public Lancamento findByCodigo(Long codigo) {
		return lancamentoRepository.findOne(codigo);
	}

	public Lancamento persist(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}

		return lancamentoRepository.save(lancamento);
	}

	public void remove(Long codigo) {
		lancamentoRepository.delete(codigo);
	}
}
