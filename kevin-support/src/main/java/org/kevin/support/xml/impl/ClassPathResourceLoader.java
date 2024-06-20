package org.kevin.support.xml.impl;

import org.kevin.support.xml.ResourceCallBack;
import org.kevin.support.xml.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * load resources with class local path.
 * @author kevin
 */
public class ClassPathResourceLoader extends AbstractResourceLoader implements ResourceLoader {
	private static Logger log = LoggerFactory.getLogger(ClassPathResourceLoader.class);
	private Class<?> clazz = this.getClass();
	
	public ClassPathResourceLoader() { super(); }
	
	/**
	 * @param clazz the class to load resource
	 */
	public ClassPathResourceLoader(Class<?> clazz) {
		if(clazz!=null) this.clazz = clazz;
	}
	@Override
	public boolean loadResource(ResourceCallBack callback, String path) throws IOException{
		boolean success = false;
		Resource resource = new ClassPathResource(path, clazz);
		log.debug(resource.getFile().getCanonicalFile().toString());
		InputStream is = resource.getInputStream();
		if(is==null){return false;}
		callback.apply(is);
		success = true;
		return success;
	}
}
