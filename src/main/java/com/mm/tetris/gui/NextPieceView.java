package com.mm.tetris.gui;

import com.mm.tetris.board.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.awt.*;

/**
 * View code for displaying the next piece
 */
@Singleton
public class NextPieceView extends BlockBoardView {

    private static Logger log = LoggerFactory.getLogger(NextPieceView.class);

    private Dimension currentPieceDimension;

    private Position currentTopLeftPosition;

    /**
     * Determine the pixel dimensions of the tetromino on the block board
     */
    private void determineDimensions() {
        // set the initial values for the actual edges at the opposite extremes so they can be adjusted
        int left = blockBoard.getWidth();
        int right = 0;
        int top = blockBoard.getHeight();
        int bottom = 0;

        for (int x = 0; x < blockBoard.getWidth(); x++){
            for (int y = 0; y < blockBoard.getHeight(); y++) {
                if (blockBoard.getBlockAt(x, y) != null) {
                    if (left > x) left = x;
                    if (right < x) right = x;
                    if (top > y) top = y;
                    if (bottom < y) bottom = y;
                }
            }
        }

        currentPieceDimension = new Dimension(
                (right - left + 1) * blockSize, (bottom - top + 1) * blockSize);
        currentTopLeftPosition = new Position(left, top);
    }

    /**
     * Paint method.  Draws the "Next:" string and translates the graphics so the block board will
     * be painted at the position that will center the next piece
     * @param g2 Graphics2D
     */
    @Override
    public void paint(Graphics2D g2) {
        if (shouldPrintText) {
            g2.setColor(textColor);
            g2.drawString(text, textPosition.getX(), textPosition.getY());
        }

        // need to translate the graphics considering view dimensions, tetromino, and null blocks
        determineDimensions();

        // determine the origin based on the piece and the view sizes
        int left = (int) ((viewDimension.getWidth() - currentPieceDimension.getWidth()) / 2);
        int top = (int) ((viewDimension.getHeight() - currentPieceDimension.getHeight()) / 2);

        // move origin for null blocks
        left -= currentTopLeftPosition.getX() * blockSize;
        top -= currentTopLeftPosition.getY() * blockSize;

        g2.translate(left, top);
        super.paint(g2);
    }
}
