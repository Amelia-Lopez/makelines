package com.mm.tetris.controller.input;

import com.google.inject.ImplementedBy;

@ImplementedBy(InputControllerImpl.class)
public interface InputController {

    void init();

    void tearDown();

    void moveLeft();

    void moveRight();

    void rotateClockwise();

    void rotateCounterclockwise();

    void dropOneRow();

    void dropCompletely();
}
