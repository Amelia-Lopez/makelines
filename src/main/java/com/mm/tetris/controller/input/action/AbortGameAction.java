package com.mm.tetris.controller.input.action;

import com.mm.tetris.controller.*;
import com.mm.tetris.gui.MessagePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Aborts the current game
 */
public class AbortGameAction extends AbstractAction
        implements GameOverObserver, ImageMessager, NewGameObserver {

    private static Logger log = LoggerFactory.getLogger(AbortGameAction.class);

    @Inject
    private Controller controller;

    @Inject
    private GameOverInformer gameOverInformer;

    @Inject
    private NewGameInformer newGameInformer;

    @Inject
    private MessagePanel messagePanel;

    // image that says "Game Over"
    private BufferedImage image;

    @Override
    public void init() {
        gameOverInformer.addGameOverObserver(this);
        newGameInformer.addNewGameObserver(this);
        setEnabled(false);
    }

    /**
     * Player wants to abort the game.  Inform the controller
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.gameOver();
    }

    /**
     * A game over occurred (either from losing the game or by this action being activated)
     */
    @Override
    public void gameOver() {
        log.info("Display image.");
        messagePanel.showMessage(image);
        setEnabled(false);
    }

    @Override
    public void setMessageImage(BufferedImage image) {
        log.info("Setting image.");
        this.image = image;
    }

    @Override
    public void newGameStarted() {
        messagePanel.hideMessage();
        setEnabled(true);
    }
}
