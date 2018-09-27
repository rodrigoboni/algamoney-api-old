package com.algaworks.algamoney.api.service;

import java.util.List;

import com.algaworks.algamoney.api.repository.filter.Filter;

public interface ServiceInterface<T, F> {
	public List<T> listAll();
	public List<T> list(F filter);
	public T findByCodigo(Long codigo);
	public T persist(T entity);
	public void remove(Long codigo);
	public T update(Long codigo, T entity);
}
