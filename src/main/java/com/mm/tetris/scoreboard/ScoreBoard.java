package com.mm.tetris.scoreboard;

import java.util.List;

public interface ScoreBoard {

	/**
	 * Add a score to the score board.  Returns true if the score made it onto the board
	 * @param score int the player's score
	 * @return boolean true if score was high enough to get onto the board
	 */
	boolean addScore(final int score);
	
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
