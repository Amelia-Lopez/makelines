package com.mariolopezjr.tetris.scoreboard;

import java.util.List;

public interface ScoreBoard {

	/**
	 * Add a score to the score board.  Returns true if the score made it onto the board
	 * @param name String the name of the player
	 * @param score int the player's score
	 * @param rows the number of rows cleared during gameplay
	 * @return boolean true if score was high enough to get onto the board
	 */
	boolean addScore(final String name, final int score, final int rows);
	
	/**
	 * Returns the scores from the score board
	 * @return List<Score>
	 */
	List<Score> getScores();
	
	/**
	 * Reset the score board
	 */
	void reset();
}
