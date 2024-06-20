package org.kevin.support.xml;

import org.kevin.support.xml.impl.Filter;

import java.io.IOException;
import java.io.InputStream;


/**
 * The interface for reading resources.
 * @author kevin
 */
public interface Resource {
	/**
	 * read resources and return InputStream.
	 * @return InputStream
	 * @throws IOException 
	 */
	public InputStream getInputStream()throws IOException;
	
	/**
	 * @return get the description of resource for debugging.
	 */
	public String getDescription();

	/**
	 * @param filter the file filter
	 */
	public void setFilter(Filter filter);
	
	/**
	 * @return the file filter
	 */
	public Filter getFilter();
	
	/**
	 * @return the full filename include the path.
	 */
	public String getFileName();
}
