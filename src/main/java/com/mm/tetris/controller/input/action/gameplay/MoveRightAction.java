package com.mm.tetris.controller.input.action.gameplay;

import com.mm.tetris.controller.TickListener;
import com.mm.tetris.controller.Ticker;
import com.mm.tetris.controller.input.InputController;

import javax.inject.Inject;

public class MoveRightAction extends GameplayAction implements TickListener {

    @Inject
    private InputController inputController;

    @Inject
    private Ticker ticker;

    /**
     * Constructor
     */
    public MoveRightAction() {
        // do nothing
    }

    /**
     * Initialize this action
     */
    @Override
    public void init() {
        // todo: this should be configured and should consider user pref for accelerated action
        ticker.setTickListener(this).setInterval(200L);
    }

    /**
     * Key was pressed
     */
    @Override
    public void keyPressed() {
        ticker.start();
    }

    /**
     * Key was released
     */
    @Override
    public void keyReleased() {
        ticker.stop();
    }

    /**
     * Execute this action
     */
    @Override
    public void tick() {
        inputController.moveRight();
    }
}
