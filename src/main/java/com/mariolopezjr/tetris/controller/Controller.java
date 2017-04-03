package com.mariolopezjr.tetris.controller;

import com.google.inject.ImplementedBy;

@ImplementedBy(MainController.class)
public interface Controller extends TickListener {

    /**
     * Start a new game
     */
    void newGame();

    void pauseGame();

    void gameOver();

    void moveLeft();

    void moveRight();

    void moveDownOneRow();

    void moveDownCompletely();

    void rotateClockwise();

    void rotateCounterClockwise();
}
