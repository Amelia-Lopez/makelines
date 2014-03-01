package com.mm.tetris.board;

import java.util.LinkedList;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;

@Singleton
public class BlockBoard {
	
	@Inject
	private Configuration config;

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
	 * Constructor
	 */
	public BlockBoard() {
		board = new Block[width][height + heightPadding];
	}
	
	
	
	
	
	
	
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Block getBlock(final int x, final int y) {
		return board[x][y];
	}
}
