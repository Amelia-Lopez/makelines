package com.mm.tetris.board;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mm.tetris.board.builder.TetrominoLoader;

@Singleton
public class TetrominoFactory {
	
	@Inject
	private TetrominoLoader tetrominoLoader;
	
	private ArrayList<Tetromino> tetrominos;
	private Random randomGenerator;
	
	/**
	 * Constructor
	 */
	public TetrominoFactory() {
		// do nothing
	}
	
	public void init() {
		tetrominos = tetrominoLoader.loadTetrominos();
		randomGenerator = new Random();
	}
	
	/**
	 * Returns a random tetromino
	 * @return Tetromino
	 */
	public Tetromino getRandomTetromino() {
		return tetrominos.get(randomGenerator.nextInt(tetrominos.size()));
	}
}
