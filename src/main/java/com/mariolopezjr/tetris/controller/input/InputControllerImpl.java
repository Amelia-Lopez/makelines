package com.mariolopezjr.tetris.controller.input;

import com.mariolopezjr.tetris.controller.*;
import com.mariolopezjr.tetris.controller.input.action.gameplay.GameplayAction;
import com.mariolopezjr.tetris.controller.input.binding.KeyBindings;
import com.mariolopezjr.tetris.gui.MainPanel;
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
public class InputControllerImpl
        implements InputController, KeyListener, GameOverObserver, NewGameObserver {

    private static Logger log = LoggerFactory.getLogger(InputControllerImpl.class);

    @Inject
    private Controller controller;

    @Inject
    private GameOverInformer gameOverInformer;

    @Inject
    private NewGameInformer newGameInformer;

    @Inject
    private MainPanel mainPanel;

    @Inject
    private KeyBindings keyBindings;

    private boolean keysAreMapped = false;

    private boolean isPlaying = true;

    /**
     * Constructor
     */
    public InputControllerImpl() {
        // do nothing
    }

    /**
     * Should be called when a new game is started
     */
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

        gameOverInformer.addGameOverObserver(this);
        newGameInformer.addNewGameObserver(this);
    }

    @Override
    public void tearDown() {
        mainPanel.removeKeyListener(this);
    }

    @Override
    public void pause() {
        isPlaying = !isPlaying;
        controller.pauseGame();
    }

    @Override
    public void moveLeft() {
        if (isPlaying) controller.moveLeft();
    }

    @Override
    public void moveRight() {
        if (isPlaying) controller.moveRight();
    }

    @Override
    public void rotateClockwise() {
        if (isPlaying) controller.rotateClockwise();
    }

    @Override
    public void rotateCounterClockwise() {
        if (isPlaying) controller.rotateCounterClockwise();
    }

    @Override
    public void dropOneRow() {
        if (isPlaying) controller.moveDownOneRow();
    }

    @Override
    public void dropCompletely() {
        if (isPlaying) controller.moveDownCompletely();
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

    @Override
    public void gameOver() {
        isPlaying = false;
    }

    @Override
    public void newGameStarted() {
        isPlaying = true;
    }
}
