package com.mm.tetris.board;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mm.tetris.board.builder.TetrominoLoader;

@Singleton
public class TetrominoFactory {
	
	private ArrayList<Tetromino> tetrominos;
	private Random randomGenerator;
	
	/**
	 * Constructor
	 */
	@Inject
	public TetrominoFactory(TetrominoLoader tetrominoLoader) {
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
