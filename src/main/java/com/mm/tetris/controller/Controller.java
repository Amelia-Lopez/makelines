package com.mm.tetris.controller;

import com.google.inject.ImplementedBy;

@ImplementedBy(MainController.class)
public interface Controller extends TickListener {

	/**
	 * Start a new game
	 */
	void newGame();

    void moveLeft();

    void moveRight();

    void moveDownOneRow();

    void moveDownCompletely();

    void rotateClockwise();

    void rotateCounterClockwise();
}
