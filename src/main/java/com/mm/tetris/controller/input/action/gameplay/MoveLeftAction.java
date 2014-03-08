package com.mm.tetris.controller.input.action.gameplay;

import com.mm.tetris.controller.TickListener;
import com.mm.tetris.controller.Ticker;
import com.mm.tetris.controller.input.InputController;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Action to move current Tetromino piece to the left
 */
public class MoveLeftAction extends GameplayAction implements TickListener {

    private static Logger log = LoggerFactory.getLogger(MoveLeftAction.class);

    @Inject
    private Configuration config;

    @Inject
    private InputController inputController;

    @Inject
    private Ticker ticker;

    /**
     * Constructor
     */
    public MoveLeftAction() {
        // do nothing
    }

    /**
     * Initialize this action
     */
    @Override
    public void init() {
        // todo: support accelerated movement
        int delay = config.getInt("movement/input/leftright/@normal");
        ticker.setTickListener(this).setInterval(delay);
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
        inputController.moveLeft();
    }
}
