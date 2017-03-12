package com.mariolopezjr.tetris.controller.input;

import com.google.inject.ImplementedBy;

@ImplementedBy(InputControllerImpl.class)
public interface InputController {

    void init();

    void tearDown();

    void pause();

    void moveLeft();

    void moveRight();

    void rotateClockwise();

    void rotateCounterClockwise();

    void dropOneRow();

    void dropCompletely();
}
