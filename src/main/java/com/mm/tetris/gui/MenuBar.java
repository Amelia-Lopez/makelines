package com.mm.tetris.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JMenuBar;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mm.tetris.util.ColorUtil;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = -7002677070179778997L;
	
	private static Logger log = LoggerFactory.getLogger(MenuBar.class);

	@Inject
	private Configuration config;
	
	@Inject
	private ColorUtil colorUtil;
	
	/**
	 * Constructor
	 */
	public MenuBar() {
		// do nothing
	}
	
	public void setupMenu() {
		// load config values
		String configSection = "gui/menubar/";
		String colorName = config.getString(configSection + "color");
		int width = config.getInt(configSection + "dimensions/width");
		int height = config.getInt(configSection + "dimensions/height");
		
		setOpaque(true);
        setBackground(colorUtil.getColorForString(colorName));
        setPreferredSize(new Dimension(width, height));
		
	}
	
	
}
