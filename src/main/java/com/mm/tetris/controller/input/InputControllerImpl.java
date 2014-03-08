package com.mm.tetris.controller.input;

import com.mm.tetris.controller.Controller;
import com.mm.tetris.controller.input.action.gameplay.GameplayAction;
import com.mm.tetris.controller.input.binding.KeyBindings;
import com.mm.tetris.gui.MainPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 *
 */
@Singleton
public class InputControllerImpl implements InputController, KeyListener {

    private static Logger log = LoggerFactory.getLogger(InputControllerImpl.class);

    @Inject
    private Controller controller;

    @Inject
    private MainPanel mainPanel;

    @Inject
    private KeyBindings keyBindings;

    private boolean keysAreMapped = false;

    /**
     * Constructor
     */
    public InputControllerImpl() {
        // do nothing
    }

    @Override
    public void init() {
        // use the panel to make this class handle input
        mainPanel.addKeyListener(this);
        mainPanel.requestFocusInWindow();

        if (!keysAreMapped) {
            // if we haven't already, load the key bindings
            keyBindings.init();

            InputMap inputMap = mainPanel.getInputMap();
            ActionMap actionMap = mainPanel.getActionMap();

            for (Map.Entry<KeyStroke, GameplayAction> keyBinding : keyBindings.getKeyBindings().entrySet()) {
                log.debug("Binding " + keyBinding.getKey());
                keyBinding.getValue().init();
                inputMap.put(keyBinding.getKey(), keyBinding.getKey());
                actionMap.put(keyBinding.getKey(), keyBinding.getValue());
            }

            keysAreMapped = true;
        }
    }

    @Override
    public void tearDown() {
        mainPanel.removeKeyListener(this);
    }

    @Override
    public void moveLeft() {
        controller.moveLeft();
    }

    @Override
    public void moveRight() {
        controller.moveRight();
    }

    @Override
    public void rotateClockwise() {
        controller.rotateClockwise();
    }

    @Override
    public void rotateCounterClockwise() {
        controller.rotateCounterClockwise();
    }

    @Override
    public void dropOneRow() {
        controller.moveDownOneRow();
    }

    @Override
    public void dropCompletely() {
        controller.moveDownCompletely();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        Object action = keyBindings.getKeyBindings().get(
                KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers()));

        if (action != null) {
            ((GameplayAction) action).stop();
        }

    }
}
