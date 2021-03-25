package amylopez.makelines.controller.input.action.gameplay;

import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;

/**
 * Input action that rotates the current tetromino clockwise
 */
public class RotateClockwiseAction extends GameplayAction {

    @Inject
    private Configuration config;

    @Override
    public void init() {
        int delay = config.getInt("movement/input/rotate/@normal");
        ticker.setTickListener(this).setInterval(delay);
    }

    @Override
    public void tick() {
        inputController.rotateClockwise();
    }
}
