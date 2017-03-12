package com.mariolopezjr.tetris.board;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mariolopezjr.tetris.board.builder.TetrominoLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TetrominoFactory {

    private static Logger log = LoggerFactory.getLogger(TetrominoFactory.class);
	
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
        log.debug("Creating new tetromino");
        return tetrominos.get(randomGenerator.nextInt(tetrominos.size()));
    }
}
