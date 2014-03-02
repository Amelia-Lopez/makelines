package com.mm.tetris.board;

import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;

@Singleton
public class BlockBoard {
	
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
	}
	
	/**
	 * Start the game
	 */
	public void start() {
		board = new Block[width][height + heightPadding];
		setNewPiece(tetrominoFactory.getRandomTetromino());
		nextPiece = tetrominoFactory.getRandomTetromino();
	}
	
	/**
	 * Puts the specified tetromino on the board
	 * @param tetromino Tetromino
	 */
	private void setNewPiece(Tetromino tetromino) {
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
			board[x][y] = tetromino.getBlock();
		}
	}
	
	
	
	
	
	
	
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Block getBlock(final int x, final int y) {
		return board[x][y + heightPadding];
	}
}
