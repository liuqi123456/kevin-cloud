package org.kevin.support.dao.impl;

import org.kevin.support.dao.BasicDao;
import org.kevin.support.entity.Entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The decorator for BasicDao, that others can extend all kinds of functions for BasicDao.
 * @author fangang
 */
public abstract class DecoratorDaoImpl implements BasicDao {

	protected BasicDao dao;

	/**
	 * @return the dao
	 */
	public BasicDao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(BasicDao dao) {
		this.dao = dao;
	}


	@Override
	public <T> void insert(T entity) {
		getDao().insert(entity);
	}

	@Override
	public <T> void update(T entity) {
		getDao().update(entity);
	}

	@Override
	public <T> void insertOrUpdate(T entity) {
		getDao().insertOrUpdate(entity);
	}

	@Override
	public <T, S extends Collection<T>> void insertOrUpdateForList(S list) {
		getDao().insertOrUpdateForList(list);
	}

	@Override
	public <T> void delete(T entity) {
		getDao().delete(entity);
	}

	@Override
	public <T, S extends Collection<T>> void deleteForList(S list) {
		getDao().deleteForList(list);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void deleteForList(Collection<S> ids, T template) {
		getDao().deleteForList(ids, template);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> T load(S id, T template) {
		return getDao().load(id, template);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> List<T> loadForList(Collection<S> ids, T template) {
		return getDao().loadForList(ids, template);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> List<T> loadAll(T template) {
		return getDao().loadAll(template);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> List<T> loadAll(List<Map<Object, Object>> colMap, T template) {
		return getDao().loadAll(colMap, template);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void delete(S id, T template) {
		getDao().delete(id, template);
	}

}