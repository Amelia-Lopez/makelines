package com.mm.tetris.controller.input.action.gameplay;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * All game play actions that are activated by a key press should extend this class
 */
public abstract class GameplayAction extends AbstractAction {

    private static Logger log = LoggerFactory.getLogger(GameplayAction.class);

    protected boolean isKeyPressed = false;

    /**
     * Called when they key is pressed
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isKeyPressed) {
            isKeyPressed = true;
            keyPressed();
        }
    }

    /**
     * Controller should call this when it detects that the key was released
     */
    public void stop() {
        isKeyPressed = false;
        keyReleased();
    }

    /**
     * If the action needs to initialize anything, it should be done here
     */
    public abstract void init();

    /**
     * Implementing class should use this to start the action
     */
    public abstract void keyPressed();

    /**
     * Implementing class should use this to stop the action
     */
    public abstract void keyReleased();
}
