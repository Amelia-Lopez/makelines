package com.mariolopezjr.tetris.controller.input.action;

import com.mariolopezjr.tetris.controller.*;
import com.mariolopezjr.tetris.gui.MessagePanel;
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
        messagePanel.showMessage(image);
        setEnabled(false);
    }

    @Override
    public void setMessageImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void newGameStarted() {
        messagePanel.hideMessage();
        setEnabled(true);
    }
}
