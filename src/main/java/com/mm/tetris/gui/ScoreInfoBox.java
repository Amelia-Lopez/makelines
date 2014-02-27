package com.mm.tetris.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mm.tetris.score.ScoreKeeper;
import com.mm.tetris.score.ScoreObserver;
import com.mm.tetris.util.ImageUtil;

/**
 * Component of the GUI that displays some sort of score information
 * (e.g. rows cleared, level, current score)
 */
public class ScoreInfoBox extends BackgroundComponent implements ScoreObserver {

	private static final long serialVersionUID = -4365114241690182039L;
	
	private static Logger log = LoggerFactory.getLogger(ScoreInfoBox.class);
	
	@Inject
	private ScoreKeeper scoreKeeper;
	
	@Inject
	private Configuration config;
	
	@Inject
	private ImageUtil imageUtil;
	
	private BufferedImage backgroundImage;
	
	private String label;
	
	private String data = "0";
	
	/**
	 * Set up this graphical component
	 * @param configPath String the base path to the config for this instance
	 */
	public void setupGui(String configPath) {
		// load config values
		int posX = config.getInt(configPath + "@x");
		int posY = config.getInt(configPath + "@y");
		int width = config.getInt(configPath + "@width");
		int height = config.getInt(configPath + "@height");
		label = config.getString(configPath + "@name");
		String backgroundFileName = config.getString(configPath + "@backgroundFile");
		
		backgroundImage = imageUtil.getImageFromFile(backgroundFileName);
		
		setSize(new Dimension(width, height));
		setLocation(new Point(posX, posY));
		backgroundColor = Color.DARK_GRAY;
		borderColor = Color.WHITE;
		
		// set up class as observer so it will be updated when the score data changes
		scoreKeeper.addObserver(this);
	}

	@Override
	public void paint(Graphics2D g2) {
		// draw the background texture image
		g2.drawImage(backgroundImage, 0, 0, this);
		
		// draw the text
		g2.setColor(Color.BLACK);
		g2.drawString(data, 30, 47);
	}

	@Override
	public void scoreUpdate(Map<String, Integer> scoreInfo) {
		data = scoreInfo.get(label).toString();
	}
}
