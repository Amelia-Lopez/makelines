package com.mm.tetris.gui;

import java.awt.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JPanel;

import com.mm.tetris.ConfigInitializable;
import org.apache.commons.configuration.Configuration;

import com.mm.tetris.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MainPanel extends JPanel {

    private static Logger log = LoggerFactory.getLogger(MainPanel.class);

	private static final long serialVersionUID = 7434660167829121638L;

	@Inject
	private Configuration config;
	
	@Inject
	private ReflectionUtil reflectionUtil;

    @Inject
    private MessagePanel messagePanel;

    @Inject
    private Background background;
	
	/**
	 * Set up this panel
	 */
	public void setupPanel() {
		// load config values
		int width = config.getInt("gui/panel/dimensions/@width");
		int height = config.getInt("gui/panel/dimensions/@height");
		
		setOpaque(true);
		setLayout(null);
		setSize(new Dimension(width, height));

		setupContents();
    }
	
	/**
	 * Populate this panel with components, set up in order for layering
	 */
	private void setupContents() {
        // set up the message panel
        messagePanel.init();
        add(messagePanel);

		// set up the block boards (primary game play area and next piece display)
        String configPath = "gui/blockboards/";
        int numOfBlockBoards = config.getInt(configPath + "@size");
        for (int position = 1; position <= numOfBlockBoards; position++) {
            String boardConfigPath = configPath + "blockboard[" + position + "]/";
            ConfigInitializable object = reflectionUtil.newInstance(
                    config.getString(boardConfigPath + "classes/@view"));
            object.init(boardConfigPath);
            add((Component) object);
        }

        // set up GUI components that display score info
		configPath = "gui/score/";
		int numOfScoreBoxes = config.getInt(configPath + "@size");
		for (int position = 1; position <= numOfScoreBoxes; position++) {
			ScoreInfoBox scoreInfoBox = reflectionUtil.newInstance(ScoreInfoBox.class);
			scoreInfoBox.setupGui(configPath + "scoreInfo[" + position + "]/");
			add(scoreInfoBox);
		}

        // background
        background.init();
        add(background);
    }
}
