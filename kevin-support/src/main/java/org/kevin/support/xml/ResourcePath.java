package org.kevin.support.xml;

import org.kevin.support.xml.impl.Filter;

import java.io.IOException;

/**
 * read resources by loader.
 * @author 范钢
 */
public interface ResourcePath {
	
	/**
	 * read resources by loader
	 * @return the array of Resources
	 * @throws IOException
	 */
	public Resource[] getResources() throws IOException;

	/**
	 * @param filter the file filter
	 */
	public void setFilter(Filter filter);
	
	/**
	 * @return the file filter
	 */
	public Filter getFilter();
}
