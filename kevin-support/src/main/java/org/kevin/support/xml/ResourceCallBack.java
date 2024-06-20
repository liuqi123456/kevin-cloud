package org.kevin.support.xml;

import java.io.InputStream;

/**
 * The callback function for reading resources.
 * @author fangang
 */
@FunctionalInterface
public interface ResourceCallBack {
	/**
	 * @param inputStream
	 */
	void apply(InputStream inputStream);
}
