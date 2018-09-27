package com.algaworks.algamoney.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.exception.PessoaInexistenteOuInativaException;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.repository.filter.Filter;
import com.algaworks.algamoney.api.repository.filter.impl.LancamentoFilter;
import com.algaworks.algamoney.api.service.ServiceInterface;

@Service
public class LancamentoService implements ServiceInterface<Lancamento, LancamentoFilter> {
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public List<Lancamento> listAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Lancamento> list(LancamentoFilter filter) {
		// utiliza o método definido na interface LancamentoRepositoryQuery
		return lancamentoRepository.filter(filter);
	}

	public Lancamento findByCodigo(Long codigo) {
		return lancamentoRepository.findOne(codigo);
	}
	
	public Lancamento persist(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public void remove(Long codigo) {
		lancamentoRepository.delete(codigo);
	}

	@Override
	public Lancamento update(Long codigo, Lancamento entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
