package org.kevin.support.xml.impl;

import org.kevin.support.xml.ResourceCallBack;
import org.kevin.support.xml.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;

/**
 * load resources with the url.
 * @author kevin
 */
public class UrlResourceLoader extends AbstractResourceLoader implements ResourceLoader {
	private static Logger log = LoggerFactory.getLogger(UrlResourceLoader.class);
	private Class<?> clazz = this.getClass();
	
	public UrlResourceLoader() { super(); }
	
	/**
	 * @param clazz the class help to load resource.
	 */
	public UrlResourceLoader(Class<?> clazz) {
		if(clazz!=null) this.clazz = clazz;
	}
	@Override
	public boolean loadResource(ResourceCallBack callback, String path) throws IOException {
		boolean success = false;
		PathMatchingResourcePatternResolver resolver = 
				new PathMatchingResourcePatternResolver(this.clazz.getClassLoader());
		Resource[] loaders = resolver.getResources(path);
		for(int i=0; i<loaders.length; i++){
			printLog(loaders[i]);
			InputStream is = loaders[i].getInputStream();
			if(is!=null){
				callback.apply(is);
				success = true;
			}
		}
		return success;
	}
	
	private void printLog(Resource resource) {
		try {
			log.debug(resource.getFile().getCanonicalPath());
		} catch (IOException e) {
			
		}
	}
}
