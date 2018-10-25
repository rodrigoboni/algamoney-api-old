package com.algaworks.algamoney.api.repository.lancamento.impl;

import com.algaworks.algamoney.api.model.Categoria_;
import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Lancamento_;
import com.algaworks.algamoney.api.model.Pessoa_;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;
import com.algaworks.algamoney.api.repository.projection.ResumoLancamento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação dos filtros / queries específicas p/ entidade lancamento
 *
 * @author s2it_rboni
 */
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Value("app.recordsPerPage")
	private String defaultRecordsPerPage;

	@Override
	public List<Lancamento> filter(LancamentoFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

		Root<Lancamento> from = criteria.from(Lancamento.class);

		criteria.where(getPredicates(filter, builder, from));

		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		setPageable(query, pageable);

		return query.getResultList();
	}

	@Override
	public List<ResumoLancamento> Quickfilter(LancamentoFilter filter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);

		final Root<Lancamento> from = criteria.from(Lancamento.class);

		criteria.select(builder.construct(ResumoLancamento.class, 
				from.get(Lancamento_.codigo), 
				from.get(Lancamento_.descricao), 
				from.get(Lancamento_.dataVencimento), 
				from.get(Lancamento_.dataPagamento),
				from.get(Lancamento_.valor), 
				from.get(Lancamento_.tipo),
				from.get(Lancamento_.categoria).get(Categoria_.nome), 
				from.get(Lancamento_.pessoa).get(Pessoa_.nome)));

		criteria.where(getPredicates(filter, builder, from));

		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		setPageable(query, pageable);

		return query.getResultList();
	}

	private void setPageable(TypedQuery<?> query, Pageable pageable) {
		int page = pageable.getPageNumber() >= 0 ? pageable.getPageNumber() : 0;
		int pageSize = pageable.getPageSize() > 0 ? pageable.getPageSize() : Integer.valueOf(defaultRecordsPerPage);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);
	}

	public Long getFilterTotalRecords(LancamentoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		Root<Lancamento> from = criteria.from(Lancamento.class);

		criteria.where(getPredicates(filter, builder, from));

		criteria.select(builder.count(from));

		return manager.createQuery(criteria).getSingleResult();
	}

	private Predicate[] getPredicates(LancamentoFilter filter, CriteriaBuilder builder, Root<Lancamento> from) {
		List<Predicate> predicates = new ArrayList<>();

		if (filter == null) {
			return predicates.toArray(new Predicate[0]);
		}

		if (!StringUtils.isEmpty(filter.getDescricao())) {
			// usando metamodel para não quebrar criteria caso nome do atributo na entidade seja alterado
			// where descricao like '%<valor>%'
			predicates.add(builder.like(builder.lower(from.get(Lancamento_.descricao)), "%" + filter.getDescricao().toLowerCase() + "%"));
		}

		if (filter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(from.get(Lancamento_.dataVencimento), filter.getDataVencimentoDe()));
		}

		if (filter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(from.get(Lancamento_.dataVencimento), filter.getDataVencimentoDe()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
