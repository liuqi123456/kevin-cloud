package org.kevin.support.cache.impl;

import org.kevin.support.cache.BasicCache;
import org.kevin.support.entity.DaoEntity;
import org.kevin.support.entity.DaoEntityHelper;
import org.kevin.support.entity.Entity;
import org.kevin.support.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;

/**
 * The cache implement for redis.
 * @author kevin
 */
public class RedisCache implements BasicCache {
	private static Logger log = LoggerFactory.getLogger(RedisCache.class);
	private static String SPLITTER = "#";
	@Autowired
	private RedisTemplate<String, Object> redisTemplateSupport;

	@Override
	public <S extends Serializable, T extends Entity<S>> void set(T entity) {
		if(entity==null) return;
		String key = generateKey(entity, entity.getId());
		log.debug("set a value object to cache: {key: "+key+", value: "+entity+"}");
		redisTemplateSupport.opsForValue().set(key, entity);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> T get(S id, T template) {
		if(id==null||template==null) return null;
		String key = generateKey(template, id);
		Object entity = redisTemplateSupport.opsForValue().get(key);
		if(entity==null) return null;
		log.debug("get a value object from cache: {key: "+key+", value: "+entity+"}");
		return object2Entity(entity, template);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void delete(S id, T template) {
		if(id==null||template==null) return;
		String key = generateKey(template, id);
		log.debug("delete a value object from cache: {key: "+key+"}");
		redisTemplateSupport.delete(key);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void setForList(Collection<T> entities) {
		if(entities==null||entities.isEmpty()) return;
		Map<String, Entity<S>> map = new HashMap<>();
		for(Entity<S> entity : entities) {
			String key = generateKey(entity, entity.getId());
			map.put(key, entity);
		}
		log.debug("set a list of value objects to cache: {values: "+entities+"}");
		redisTemplateSupport.opsForValue().multiSet(map);
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> List<T> getForList(Collection<S> ids, T template) {
		if(ids==null||template==null) return null;
		List<String> keys = new ArrayList<>();
		for(S id : ids) {
			String key = generateKey(template, id);
			keys.add(key);
		}
		List<Object> values = redisTemplateSupport.opsForValue().multiGet(keys);
		if(values==null) return null;
		List<T> entities = new ArrayList<>();
		for(Object value : values) {
			T entity = object2Entity(value, template);
			entities.add(entity);
		}
		log.debug("get a list of value objects from cache: {keys: "+keys+", values: "+entities+"}");
		return entities;
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void deleteForList(Collection<S> ids, T template) {
		if(ids==null||template==null) return;
		List<String> keys = new ArrayList<>();
		for(S id : ids) {
			String key = generateKey(template, id);
			keys.add(key);
		}
		log.debug("delete a list of value objects from cache: {keys: "+keys+"}");
		redisTemplateSupport.delete(keys);
	}
	
	/**
	 * @param obj
	 * @param template
	 * @return convert an object to entity
	 */
	@SuppressWarnings("unchecked")
	private <S extends Serializable, T extends Entity<S>> T object2Entity(Object obj, T template) {
		if(obj==null||template==null) return null;
		if(!template.getClass().equals(obj.getClass()))
			throw new DaoException("the object must be an entity["+obj.getClass()+"]");
		return (T)obj;
	}
	
	/**
	 * @param entity the value object
	 * @param id
	 * @return generate the key with "className#id" rule.
	 */
	private <S extends Serializable, T extends Entity<S>> String generateKey(T entity, S id) {
		String clazz = entity.getClass().getName();
		return clazz + SPLITTER + id;
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void setList(T template, List<T> list) {
		for(T entity : list) {
			String cacheKey = generateKey(template);
			redisTemplateSupport.opsForList().leftPush(cacheKey, entity);
			log.debug("left push value to cache: {key: "+cacheKey+"}");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Serializable, T extends Entity<S>> List<T> getList(T template) {
		String cacheKey = generateKey(template);
		List<T> list = new ArrayList<>();
		long size = redisTemplateSupport.opsForList().size(cacheKey);
		if(size==0) return list;
		List<Object> values = redisTemplateSupport.opsForList().range(cacheKey, 0, size);
		log.debug("read list of values from cache: {key: "+cacheKey+", size: "+size+"}");
		values.forEach(value->list.add((T)value));
		return list;
	}

	@Override
	public <S extends Serializable, T extends Entity<S>> void deleteList(T template) {
		String cacheKey = generateKey(template);
		redisTemplateSupport.delete(cacheKey);
		log.debug("delete the list of values from cache: {key: "+cacheKey+"}");
	}
	
	/**
	 * @param entity
	 * @return generate the key with "className#value[0]#value[1]#..." rule.
	 */
	private <S extends Serializable, T extends Entity<S>> String generateKey(T entity) {
		String clazz = entity.getClass().getName();
		DaoEntity daoEntity = DaoEntityHelper.readDataFromEntity(entity);
		StringBuffer buffer = new StringBuffer(clazz);
		daoEntity.getColMap().forEach(map -> buffer.append(SPLITTER + map.get("value")));
		return buffer.toString();
	}
}
