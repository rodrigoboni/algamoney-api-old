package com.algaworks.algamoney.api.repository.filter.impl;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.algaworks.algamoney.api.repository.filter.Filter;

/**
 * Bean para receber filtros de pesquisa da entidade lancamento
 * @author s2it_rboni
 *
 */
public class LancamentoFilter extends Filter {
	private String descricao;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dataVencimentoDe;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dataVencimentoAte;
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public LocalDate getDataVencimentoDe() {
		return dataVencimentoDe;
	}
	
	public void setDataVencimentoDe(LocalDate dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}
	
	public LocalDate getDataVencimentoAte() {
		return dataVencimentoAte;
	}
	
	public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}
}
