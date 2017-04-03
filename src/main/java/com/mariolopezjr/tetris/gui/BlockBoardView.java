package com.mariolopezjr.tetris.gui;

import java.awt.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mariolopezjr.tetris.board.BasicBlockBoard;
import com.mariolopezjr.tetris.board.Position;
import com.mariolopezjr.tetris.ConfigInitializable;
import org.apache.commons.configuration.Configuration;

import com.mariolopezjr.tetris.board.Block;
import com.mariolopezjr.tetris.util.ReflectionUtil;

/**
 * View code for the BlockBoard that uses Java2D
 */
@Singleton
public class BlockBoardView extends BackgroundComponent implements Paintable, ConfigInitializable {

    private static final long serialVersionUID = -8889994001173750284L;

    @Inject
    private Configuration config;

    @Inject
    private ReflectionUtil reflectionUtil;

    @Inject
    protected BasicBlockBoard blockBoard;

    protected Dimension viewDimension;

    protected String text;

    protected boolean shouldPrintText = false;

    protected Color textColor;

    protected Position textPosition;

    protected int blockSize;

    /**
     * Constructor
     */
    public BlockBoardView() {
        // do nothing
    }

    /**
     * Sets up this view based on configuration values
     */
    public void init(String configPath) {
        // color
        backgroundColor = reflectionUtil.getColor(config.getString(configPath + "@color"));
        borderColor = reflectionUtil.getColor(config.getString(configPath + "@border_color"));

        // size and location
        int width = config.getInt(configPath + "dimensions/@width");
        int height = config.getInt(configPath + "dimensions/@height");
        int x = config.getInt(configPath + "position/@x");
        int y = config.getInt(configPath + "position/@y");
        setSize(viewDimension = new Dimension(width, height));
        setLocation(new Point(x, y));

        // text
        text = config.getString(configPath + "@name");
        shouldPrintText = config.getBoolean(configPath + "string/@show_name");
        textColor = reflectionUtil.getColor(config.getString(configPath + "string/@color"));
        int textX = config.getInt(configPath + "string/@x");
        int textY = config.getInt(configPath + "string/@y");
        textPosition = new Position(textX, textY);

        // backing data model
        blockBoard = reflectionUtil.newInstance(config.getString(configPath + "classes/@model"));

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
        // don't draw on the border
        g2.translate(1, 1);

        for (int x = 0; x < blockBoard.getWidth(); x++) {
            for (int y = 0; y < blockBoard.getHeight(); y++) {
                Block block = blockBoard.getBlockAt(x, y);
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
