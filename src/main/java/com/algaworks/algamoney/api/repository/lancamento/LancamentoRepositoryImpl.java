package com.algaworks.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Lancamento_;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;

/**
 * Implementação dos filtros / queries específicas p/ entidade lancamento
 * 
 * @author s2it_rboni
 *
 */
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Lancamento> filtrar(LancamentoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

		Root<Lancamento> from = criteria.from(Lancamento.class);

		Predicate[] predicates = getPredicates(filter, builder, from);
		criteria.where(predicates);

		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		return query.getResultList();
	}

	private Predicate[] getPredicates(LancamentoFilter filter, CriteriaBuilder builder, Root<Lancamento> from) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (filter == null) {
			return predicates.toArray(new Predicate[0]);
		}

		if (!StringUtils.isEmpty(filter.getDescricao())) {
			//usando metamodel para não quebrar criteria caso nome do atributo na entidade seja alterado
			//where descricao like '%<valor>%'
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
