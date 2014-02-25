package com.mm.tetris.gui;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JFrame;

import org.apache.commons.configuration.Configuration;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 16467159141850531L;

	@Inject
	private Configuration config;
	
	/**
	 * Constructor
	 */
	public MainWindow() {
		super("Tetris Max");
	}
	
	/**
	 * Set up the GUI
	 */
	public void setupGUI() {
		// load config values
		String configSection = "gui/window/dimensions/";
		int width = config.getInt(configSection + "width");
		int height = config.getInt(configSection + "height");
		int widthPadding = config.getInt(configSection + "width_padding");
		int heightPadding = config.getInt(configSection + "height_padding");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		setSize(new Dimension(width + widthPadding, height + heightPadding));
		
		setVisible(true);
	}
}
