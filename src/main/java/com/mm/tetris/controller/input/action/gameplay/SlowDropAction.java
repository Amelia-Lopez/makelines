package com.mm.tetris.controller.input.action.gameplay;

import com.mm.tetris.controller.TickListener;
import com.mm.tetris.controller.Ticker;
import com.mm.tetris.controller.input.InputController;
import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;

public class SlowDropAction extends GameplayAction implements TickListener {

    @Inject
    private Configuration config;

    @Inject
    private InputController inputController;

    @Inject
    private Ticker ticker;


    @Override
    public void init() {
        int delay = config.getInt("movement/input/drop/@normal");
        ticker.setTickListener(this).setInterval(delay);
    }

    @Override
    public void keyPressed() {
        ticker.start();
    }

    @Override
    public void keyReleased() {
        ticker.stop();
    }

    @Override
    public void tick() {
        inputController.dropOneRow();
    }
}
