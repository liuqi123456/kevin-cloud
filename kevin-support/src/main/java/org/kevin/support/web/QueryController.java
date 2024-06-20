package org.kevin.support.web;

import org.kevin.support.entity.ResultSet;
import org.kevin.support.exception.QueryException;
import org.kevin.support.service.QueryService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * The generic query controller
 * @author fangang
 */
@RestController
public class QueryController {

	@Autowired
	private ApplicationContext applicationContext = null;

	@RequestMapping(value="query/{bean}", method= {RequestMethod.GET, RequestMethod.POST})
	public ResultSet query(@PathVariable("bean")String beanName,
						   @RequestBody(required=false) Map<String, Object> params) {
		QueryService service = getBean(beanName);
		return service.query(params);
	}
	
	/**
	 * get bean in the spring context by name.
	 * @param name the bean name
	 * @return the instance of the bean
	 */
	private QueryService getBean(String name) {
		if(name==null||name.isEmpty()) throw new QueryException("The bean name is empty!");
		try {
			return (QueryService)applicationContext.getBean(name);
		} catch (NoSuchBeanDefinitionException e) {
			throw new QueryException("No such bean definition in the spring context!", e);
		} catch (BeansException e) {
			throw new QueryException("error when get the bean["+name+"]");
		}
	}
}
