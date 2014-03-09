package com.mm.tetris.board;

import javax.inject.Singleton;

/**
 * The block board for the next piece
 */
@Singleton
public class NextPieceBlockBoard extends BasicBlockBoard {

    /**
     * Constructor, set up the small possible block board to represent the next piece
     */
    public NextPieceBlockBoard() {
        width = 4;
        height = 4;
        heightPadding = 0;
    }

    /**
     * Set the next piece
     * @param tetromino Tetromino the next piece
     */
    public void setNextPiece(Tetromino tetromino) {
        // clear the board
        super.init();

        for (Position position : tetromino.getPositions()) {
            setBlockAt(
                    tetromino.getBlock(),
                    position.getX(),
                    position.getY());
        }
    }
}
