package com.algaworks.algamoney.api.repository.lancamento;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface para queries específicas do repositório da entidade Lancamento
 *
 * @author s2it_rboni
 */
public interface LancamentoRepositoryQuery { // o nome da interface tem que ser igual ao nome do repositório com sufixo "Query"

	public List<Lancamento> filter(LancamentoFilter filter, Pageable pageable);

	public List<ResumoLancamento> Quickfilter(LancamentoFilter filter, Pageable pageable);

	public Long getFilterTotalRecords(LancamentoFilter filter);
}
