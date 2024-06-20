package org.kevin.support.xml.impl;

/**
 * The file filter
 * @author kevin
 */
public abstract class Filter {
	
	/**
	 * @param fileName the filename pattern such as "*.xml"
	 * @return whether satisfied the filename pattern.
	 */
	public abstract boolean isSatisfied(String fileName);
}
