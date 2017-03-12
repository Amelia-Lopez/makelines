package com.mariolopezjr.tetris.controller.input.action.gameplay;

public class HardDropAction extends GameplayAction {

    @Override
    public void init() {
        // do not set up ticker since this is not a repeatable action
    }

    /**
     * Override this method since this is a one time action and not repeatable
     */
    @Override
    public void keyPressed() {
        inputController.dropCompletely();
    }

    @Override
    public void keyReleased() {
        // do nothing
    }

    @Override
    public void tick() {
        // do nothing
    }
}
