package com.mm.tetris.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;

import com.mm.tetris.board.Block;
import com.mm.tetris.board.BlockBoard;
import com.mm.tetris.util.ReflectionUtil;

/**
 * View code for the BlockBoard that uses Java2D
 */
@Singleton
public class BlockBoardView extends BackgroundComponent implements Paintable {

	private static final long serialVersionUID = -8889994001173750284L;
	
	@Inject
	private Configuration config;
	
	@Inject
	private ReflectionUtil reflectionUtil;
	
	@Inject
	private BlockBoard blockBoard;
	
	private int blockSize;
	
	/**
	 * Constructor
	 */
	public BlockBoardView() {
		// do nothing
	}
	
	/**
	 * Sets up this view based on configuration values
	 */
	public void init() {
		String configPath = "gui/blockboard/";
		
		// color
		backgroundColor = reflectionUtil.getColor(config.getString(configPath + "@color"));
		borderColor = reflectionUtil.getColor(config.getString(configPath + "@border_color"));
		
		// size and location
		int width = config.getInt(configPath + "dimensions/@width");
		int height = config.getInt(configPath + "dimensions/@height");
		int x = config.getInt(configPath + "position/@x");
		int y = config.getInt(configPath + "position/@y");
		setSize(new Dimension(width, height));
		setLocation(new Point(x, y));
		
		// block size
		blockSize = config.getInt("gui/block/@length");
		
		// initialize data model
		blockBoard.init();
	}

	/**
	 * Paints all of the blocks
	 */
	@Override
	public void paint(Graphics2D g2) {
		for (int x = 0; x < blockBoard.getWidth(); x++) {
			for (int y = 0; y < blockBoard.getHeight(); y++) {
				Block block = blockBoard.getBlock(x, y);
				if (block != null) {
					block.paint(g2);
				}
				// move the y of the origin so the block renders in the correct position
				g2.translate(0, blockSize);
			}
			
			// move the x of the origin, reset the y
			g2.translate(blockSize, 0 - (blockSize * blockBoard.getHeight()));
		}
		
		// reset the x of the origin, y should already be reset at this point
		g2.translate(0 - (blockSize * blockBoard.getWidth()), 0);

	}

}
