package com.mm.tetris;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class MainModule extends AbstractModule {
	
	private static Logger log = LoggerFactory.getLogger(MainModule.class);

	@Override
	protected void configure() {
		
	}
	
	@Provides
	public Configuration providesConfiguration() {
		CombinedConfiguration config = null;
		
		try {
			DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder("config/config.xml");
			config = builder.getConfiguration(true);
			config.setExpressionEngine(new XPathExpressionEngine());
		} catch (Throwable t) {
			// don't bother running the application if we can't load configuration
			log.error("Unable to load configuration.", t);
			System.exit(1);
		}
		
		return config;
	}

}
