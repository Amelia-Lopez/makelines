package com.mm.tetris.board;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BlockBoard {

    private static Logger log = LoggerFactory.getLogger(BlockBoard.class);
	
	@Inject
	private Configuration config;
	
	@Inject
	private TetrominoFactory tetrominoFactory;

	/**
	 * Number of blocks
	 */
	private int width = 10;
	private int height = 20;
	private int heightPadding = 3;
	
	/**
	 * The board
	 */
	private Block[][] board;
	
	/**
	 * The four falling blocks
	 */
	private LinkedList<Position> fallingBlocks;
	
	/**
	 * The length of the tetromino (2, 3 or 4).  Used when rotating the falling
	 * tetromino  
	 */
	private int tetrominoLength;
	
	/**
	 * The upper-left point of the tetromino area used when rotating
	 */
	private Position rotatePosition;
	
	/**
	 * The next piece
	 */
	private Tetromino nextPiece;

	
	/**
	 * Constructor
	 */
	public BlockBoard() {
		// do nothing
	}
	
	public void init() {
		tetrominoFactory.init();
		board = new Block[width][height + heightPadding];
        rotatePosition = new Position();
	}
	
	/**
	 * Start the game
	 */
	public void start() {
		board = new Block[width][height + heightPadding];
        putNewPieceOnBlockBoard(tetrominoFactory.getRandomTetromino());
		nextPiece = tetrominoFactory.getRandomTetromino();
	}

    /**
     * Drop the falling blocks one row.  Return false if unable to.
     * @return TetrominoDropResult DROPPED if tetromino dropped, SET if tetromino was set and the
     * next Tetromino was placed on the board
     */
    public TetrominoDropResult dropOneRow() {
        if (canCurrentTetrominoFall()) {
            dropCurrentTetromino();
            return TetrominoDropResult.DROPPED;
        } else {
            setCurrentTetromino();
            return TetrominoDropResult.SET;
        }
    }

    /**
     * Puts the Next Tetromino on the board and creates a new next tetromino
     */
    public void loadNextTetromino() {
        putNewPieceOnBlockBoard(nextPiece);
        nextPiece = tetrominoFactory.getRandomTetromino();
    }

    /**
     * Returns the list of completed rows
     * @return List<Integer> rows that are complete, empty list if no rows are complete
     */
    public List<Integer> getCompletedRows() {
        List<Integer> completedRows = new LinkedList<>();

        for (int row = 0; row < height; row++) {
            boolean rowComplete = true;

            for (int col = 0; col < width; col++) {
                if (getBlockAt(col, row) == null) {
                    rowComplete = false;
                    break;
                }
            }

            if (rowComplete)
                completedRows.add(row);
        }

        return completedRows;
    }

    /**
     * Remove the specified rows and move the rows above it down
     * @param rows List<Integer>
     */
    public void clearRows(final List<Integer> rows) {
        if (rows.isEmpty()) {
            return;
        }

        // check if we can quickly clear a chunk of rows
        boolean isContinuousRows = rows.size() == 1 || rows.size() == 4 ||
                (rows.size() == 2 && diffIsOne(rows.get(0), rows.get(1))) ||
                (rows.size() == 3 && diffIsOne(rows.get(0), rows.get(1)) && diffIsOne(rows.get(1), rows.get(2)));

        if (isContinuousRows) {
            // start from the row above the one(s) we want to clear
            for (int row = rows.get(rows.size() - 1) - rows.size(); row >= 0; row--) {
                for (int col = 0; col < width; col++) {
                    setBlockAt(getBlockAt(col, row), col, row + rows.size());
                }
            }
        } else {
            // slower method, clears each row one by one
            for (int rowToClear : rows) {
                for (int row = rowToClear - 1; row >= 0; row--) {
                    for (int col = 0; col < width; col++) {
                        setBlockAt(getBlockAt(col, row), col, row + 1);
                    }
                }
            }
        }
    }

    /**
     * Move the current tetromino to the left if possible
     */
    public void moveCurrentTetrominoLeft() {
        log.debug("moving piece left");
        if (canMoveLeft()) {
            // save a block
            Block block = getBlockAt(fallingBlocks.get(0).getX(), fallingBlocks.get(0).getY());

            // clear the current blocks
            for (Position fallingBlock : fallingBlocks) {
                setBlockAt(null, fallingBlock.getX(), fallingBlock.getY());
                fallingBlock.setX(fallingBlock.getX() - 1);
            }

            // set new positions for falling blocks
            for (Position fallingBlock : fallingBlocks) {
                setBlockAt(block, fallingBlock.getX(), fallingBlock.getY());
            }

            // move the rotation position to the left
            rotatePosition.setX(rotatePosition.getX() - 1);
        }
    }

    /**
     * Move the current tetromino to the right if possible
     */
    public void moveCurrentTetrominoRight() {
        log.debug("moving piece right");
        if (canMoveRight()) {
            // save a block
            Block block = getBlockAt(fallingBlocks.get(0).getX(), fallingBlocks.get(0).getY());

            // clear the current blocks
            for (Position fallingBlock : fallingBlocks) {
                setBlockAt(null, fallingBlock.getX(), fallingBlock.getY());
                fallingBlock.setX(fallingBlock.getX() + 1);
            }

            // set new positions for falling blocks
            for (Position fallingBlock : fallingBlocks) {
                setBlockAt(block, fallingBlock.getX(), fallingBlock.getY());
            }

            // move the rotation position to the left
            rotatePosition.setX(rotatePosition.getX() + 1);
        }
    }

    /**
     * Rotate the current falling tetromino clockwise
     */
    public void rotateClockwise() {
        Block block = getBlockAt(fallingBlocks.get(0).getX(), fallingBlocks.get(0).getY());
        LinkedList<Position> newFallingBlocks = new LinkedList<>();

        for (Position currentPos : fallingBlocks) {
            // determine new position for each block
            int deltaX = currentPos.getX() - rotatePosition.getX();
            int deltaY = currentPos.getY() - rotatePosition.getY();
            int newX = tetrominoLength - 1 - deltaY + rotatePosition.getX();
            int newY = deltaX + rotatePosition.getY();

            // verify this is a valid position
            if (!isValidPosition(newX, newY)) return;
            if (!isAvailablePosition(newX, newY)) return;

            // add new block position to new list of falling blocks (for later creation)
            newFallingBlocks.push(new Position(newX, newY));
        }

        // if we got to this point, remove the old blocks
        for (Position currentPos : fallingBlocks) {
            setBlockAt(null, currentPos.getX(), currentPos.getY());
        }

        // add the blocks back in their rotated position
        for (Position newPos : newFallingBlocks) {
            setBlockAt(block, newPos.getX(), newPos.getY());
        }

        fallingBlocks = newFallingBlocks;
    }

    /**
     * Rotate the current falling tetromino counter-clockwise
     */
    public void rotateCounterClockwise() {
        Block block = getBlockAt(fallingBlocks.get(0).getX(), fallingBlocks.get(0).getY());
        LinkedList<Position> newFallingBlocks = new LinkedList<>();

        for (Position currentPos : fallingBlocks) {
            // determine new position for each block
            int deltaX = currentPos.getX() - rotatePosition.getX();
            int deltaY = currentPos.getY() - rotatePosition.getY();
            int newX = deltaY + rotatePosition.getX();
            int newY = tetrominoLength - 1 - deltaX + rotatePosition.getY();

            // verify this is a valid position
            if (!isValidPosition(newX, newY)) return;
            if (!isAvailablePosition(newX, newY)) return;

            // add new block position to new list of falling blocks (for later creation)
            newFallingBlocks.push(new Position(newX, newY));
        }

        // if we got to this point, remove the old blocks
        for (Position currentPos : fallingBlocks) {
            setBlockAt(null, currentPos.getX(), currentPos.getY());
        }

        // add the blocks back in their rotated position
        for (Position newPos : newFallingBlocks) {
            setBlockAt(block, newPos.getX(), newPos.getY());
        }

        fallingBlocks = newFallingBlocks;
    }

    /**
     * Determines if new tetrominos can still be created on the board.  If this method returns
     * false, then it should be game over.
     * @return boolean true if game can continue, false if game over
     */
    public boolean isAbleToCreateNewTetromino() {
        int rowAboveTop = -1;

        // verify the spawn positions are all available
        for (int currentColumn = 0; currentColumn < width; currentColumn++) {
            if (!isAvailablePosition(currentColumn, rowAboveTop))
                return false;
        }

        return true;
    }

    /**
     * Determines if the specified position exists on the block board
     * @param x int
     * @param y int
     * @return boolean true if valid position that won't throw a null pointer exception if used
     */
    private boolean isValidPosition(int x, int y) {
        return (x >= 0 && x < width && y >= (0 - heightPadding) && y < height);
    }

    /**
     * Determines if the specified position is available.  To be available the position must either
     * not contain a block or it must have a block that is currently falling.
     * @param x int
     * @param y int
     * @return boolean true if the specified position is available for a new block
     */
    private boolean isAvailablePosition(int x, int y) {
        Block block = getBlockAt(x, y);
        return (block == null || block.isFalling());
    }

    /**
     * Puts the specified tetromino on the board
     * @param tetromino Tetromino
     */
    private void putNewPieceOnBlockBoard(Tetromino tetromino) {
        fallingBlocks = new LinkedList<>();
        int deltaX, deltaY;

        // position the piece in the middle with one row shown
        rotatePosition.setX(deltaX = (width / 2) - (tetromino.getLength() / 2));
        rotatePosition.setY(deltaY = 1 - tetromino.getLength());

        // set the falling blocks and update the board with the block textures
        for (Position pos : tetromino.getPositions()) {
            int x = pos.getX() + deltaX;
            int y = pos.getY() + deltaY;
            fallingBlocks.add(new Position(x, y));
            setBlockAt(tetromino.getBlock(), x, y);
        }

        // set the length, used for rotations
        tetrominoLength = tetromino.getLength();
    }

    /**
     * Verify current tetromino can move one column to the left
     * @return boolean true if tetromino can move left
     */
    private boolean canMoveLeft() {
        for (Position fallingBlock : fallingBlocks) {
            // check for edge of board
            if (fallingBlock.getX() == 0)
                return false;

            if (!isAvailablePosition(fallingBlock.getX() - 1, fallingBlock.getY()))
                return false;
        }

        return true;
    }

    /**
     * Verify current tetromino can move one column to the right
     * @return boolean true if tetromino can move right
     */
    private boolean canMoveRight() {
        for (Position fallingBlock : fallingBlocks) {
            // check for edge of board
            if (fallingBlock.getX() == width - 1)
                return false;

            if (!isAvailablePosition(fallingBlock.getX() + 1, fallingBlock.getY()))
                return false;
        }

        return true;
    }

    /**
     * Determines if the difference between the two specified integers is 1.
     * @param x1 int
     * @param x2 int
     * @return boolean true if the difference is 1
     */
    private boolean diffIsOne(int x1, int x2) {
        return (x1 - x2 == 1 || x1 - x2 == -1);
    }

    /**
     * Check if the currently falling blocks are allowed to move down one row.
     * @return boolean true if falling pieces can fall
     */
    private boolean canCurrentTetrominoFall() {
        for (Position fallingBlock : fallingBlocks) {
            if (fallingBlock.getY() == height - 1)
                return false;

            if (!isAvailablePosition(fallingBlock.getX(), fallingBlock.getY() + 1))
                return false;
        }

        return true;
    }

    /**
     * Drop the current falling blocks one row
     */
    private void dropCurrentTetromino() {
        // save a block
        Block block = getBlockAt(fallingBlocks.get(0).getX(), fallingBlocks.get(0).getY());

        // clear the current blocks
        for (Position fallingBlock : fallingBlocks) {
            setBlockAt(null, fallingBlock.getX(), fallingBlock.getY());
            fallingBlock.setY(fallingBlock.getY() + 1);
        }

        // set new positions for falling blocks
        for (Position fallingBlock : fallingBlocks) {
            setBlockAt(block, fallingBlock.getX(), fallingBlock.getY());
        }

        // move the rotation position down one row
        rotatePosition.setY(rotatePosition.getY() + 1);
    }

    /**
     * Stops the current falling blocks
     */
    private void setCurrentTetromino() {
        Block block = getBlockAt(fallingBlocks.get(0).getX(), fallingBlocks.get(0).getY()).clone();
        block.stopFalling();

        for (Position fallingBlock : fallingBlocks) {
            setBlockAt(block, fallingBlock.getX(), fallingBlock.getY());
        }
    }

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Block getBlockAt(final int x, final int y) {
		return board[x][y + heightPadding];
	}

    private void setBlockAt(Block block, int x, int y) {
        board[x][y + heightPadding] = block;
    }

    public static enum TetrominoDropResult {
        DROPPED,
        SET
    }
}
