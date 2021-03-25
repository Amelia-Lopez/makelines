package amylopez.makelines.controller.input.action.gameplay;

import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;

public class MoveRightAction extends GameplayAction {

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
        inputController.moveRight();
    }
}
