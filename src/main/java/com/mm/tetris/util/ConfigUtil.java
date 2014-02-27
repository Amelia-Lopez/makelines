package com.mm.tetris.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
	
	private static Logger log = LoggerFactory.getLogger(ConfigUtil.class);
	
	@Inject
	private Configuration config;

	/**
	 * Get the number of child elements of the specified config node 
	 * @param configPath String the config path, e.g. "gui/window/dimensions"
	 * @return int 0 if the config node has no children
	 */
	@SuppressWarnings("rawtypes")
	public int getNumberOfElements(final String configPath) {
		int size = 0;

		try {
			Method method = HierarchicalConfiguration.class.getDeclaredMethod(
					"fetchNodeList",
					String.class);
			method.setAccessible(true);
			Object value = method.invoke(config, "count(" + configPath + ")");
			size = ((Double) ((List) value).get(0)).intValue();
			
			log.debug("Number of elements for config [" + configPath + "] is " + size);
		} catch (Throwable t) {
			log.warn("Unable to determine number of child nodes for " + configPath, t);
		}
		
		return size;
	}
}
