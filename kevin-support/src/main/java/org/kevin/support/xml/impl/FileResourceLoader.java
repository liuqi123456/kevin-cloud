package org.kevin.support.xml.impl;

import org.kevin.support.xml.Resource;
import org.kevin.support.xml.ResourceCallBack;
import org.kevin.support.xml.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * load resources with the file path.
 * @author kevin
 */
public class FileResourceLoader extends AbstractResourceLoader implements ResourceLoader {

	@Override
	public boolean loadResource(ResourceCallBack callback, String path) throws IOException {
		boolean success = false;
		FileResource loader = new FileResource(new File(path));
		loader.setFilter(this.getFilter());
		Resource[] loaders = loader.getResources();
		if(loaders==null){return false;}
		for(int i=0; i<loaders.length; i++){
			InputStream is = loaders[i].getInputStream();
			if(is!=null){
				callback.apply(is);
				success = true;
			}
		}
		return success;
	}
}
