package com.mm.tetris.controller.input.action.gameplay;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Action to move current Tetromino piece to the left
 */
public class MoveLeftAction extends GameplayAction {

    private static Logger log = LoggerFactory.getLogger(MoveLeftAction.class);

    @Inject
    private Configuration config;

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
     * Execute this action
     */
    @Override
    public void tick() {
        inputController.moveLeft();
    }
}
