package org.kevin.support.repository;

import org.kevin.support.dao.BasicDao;
import org.kevin.support.entity.Entity;
import org.kevin.support.entity.ResultSet;
import org.kevin.support.exception.QueryException;
import org.kevin.support.factory.*;
import org.kevin.support.service.impl.QueryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The implement of the query service that 
 * it auto fill each of the object property of the item of the query result set, like this: 
 * fill the addresses of each the customer of the result set.
 * @author kevin
 */
public class AutofillQueryServiceImpl extends QueryServiceImpl {

	private BasicDao dao;
	/**
	 * @return the dao
	 */
	public BasicDao getDao() {
		if(dao==null) throw new QueryException("The dao is null");
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(BasicDao dao) {
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ResultSet afterQuery(Map<String, Object> params, ResultSet resultSet) {
		//if no result, do nothing.
		List<?> list = resultSet.getData();
		if(list==null||list.isEmpty())
			return super.afterQuery(params, resultSet);
		
		//if the value object hasn't any join, do nothing.
		Object firstOfVo = list.get(0);
		
		//auto fill value objects for each joins.
		List<Join> listOfJoins = listOfJoins(firstOfVo);
		if(listOfJoins!=null&&!listOfJoins.isEmpty()) {
			for(Join join : listOfJoins) {
				//TODO have exception
				autoFillJoin((List<Entity<Serializable>>)list, join);
			}
		}
		
		List<Ref> listOfRefs = listOfRefs(firstOfVo);
		if(listOfRefs!=null&&!listOfRefs.isEmpty()) {
			for(Ref ref : listOfRefs) {
				//TODO have exception
				autoFillRef((List<Entity<Serializable>>)list, ref);
			}
		}
		
		return super.afterQuery(params, resultSet);
	}
	
	/**
	 * list all of the joins in the value object.
	 * @param vo the value object
	 * @return the list of joins
	 */
	private List<Join> listOfJoins(Object vo) {
		VObj vObj = VObjFactory.getVObj(vo.getClass().getName());
		return vObj.getJoins();
	}
	
	/**
	 * list all of the refs in the value object.
	 * @param vo the value object
	 * @return the list of joins
	 */
	private List<Ref> listOfRefs(Object vo) {
		VObj vObj = VObjFactory.getVObj(vo.getClass().getName());
		return vObj.getRefs();
	}
	
	/**
	 * auto fill all of the joins in the value object.
	 * @param list
	 * @param join
	 */
	private <S extends Serializable, T extends Entity<S>> void autoFillJoin(List<T> list, Join join) {
		if(list==null||list.isEmpty()||join==null) return;
		GenericEntityFactoryForList<S, T> factory = new GenericEntityFactoryForList<>();
		factory.build(join, list, getDao());
	}
	
	@Autowired
	private ApplicationContext context;
	
	/**
	 * auto fill all of the joins in the value object.
	 * @param list
	 * @param ref
	 */
	private <S extends Serializable, T extends Entity<S>> void autoFillRef(List<T> list, Ref ref) {
		if(list==null||list.isEmpty()||ref==null) return;
		ReferenceFactoryForList<S, T> factory = new ReferenceFactoryForList<>(context);
		factory.build(ref, list);
	}
}
