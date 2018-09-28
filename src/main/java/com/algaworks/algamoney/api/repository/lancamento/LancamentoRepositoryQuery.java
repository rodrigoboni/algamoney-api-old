package com.algaworks.algamoney.api.repository.lancamento;

import java.util.List;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;

/**
 * Interface para queries específicas do repositório da entidade Lancamento
 * @author s2it_rboni
 *
 */
public interface LancamentoRepositoryQuery { // o nome da interface tem que ser igual ao nome do repositório com sufixo "Query"
	public List<Lancamento> filter(LancamentoFilter filter);
}
