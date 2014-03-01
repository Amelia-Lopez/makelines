package com.mm.tetris.gui;

import java.awt.Dimension;

import javax.inject.Inject;
import javax.swing.JPanel;

import org.apache.commons.configuration.Configuration;

import com.mm.tetris.util.ReflectionUtil;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 7434660167829121638L;

	@Inject
	private Configuration config;
	
	@Inject
	private ReflectionUtil reflectionUtil;
	
	@Inject
	private BlockBoardView blockBoardView;
	
	/**
	 * Set up this panel
	 */
	public void setupPanel() {
		// load config values
		int width = config.getInt("gui/panel/dimensions/@width");
		int height = config.getInt("gui/panel/dimensions/@height");
		
		setBackground(reflectionUtil.getColor(
				config.getString("gui/panel/@color")));
		
		setOpaque(true);
		setLayout(null);
		setSize(new Dimension(width, height));
		
		setupContents();
	}
	
	/**
	 * Populate this panel with components
	 */
	private void setupContents() {
		// set up the main board of blocks
		blockBoardView.init();
		add(blockBoardView);
		
		// set up GUI components that display score info
		String configPath = "gui/score/";
		int numOfScoreBoxes = config.getInt(configPath + "@size");
		for (int position = 1; position <= numOfScoreBoxes; position++) {
			ScoreInfoBox scoreInfoBox = reflectionUtil.newInstance(ScoreInfoBox.class);
			scoreInfoBox.setupGui(configPath + "scoreInfo[" + position + "]/");
			add(scoreInfoBox);
		}
	}
}
