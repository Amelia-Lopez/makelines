package com.mm.tetris.controller.input.action;

import com.mm.tetris.controller.*;
import com.mm.tetris.controller.input.action.AbstractAction;

import javax.inject.Inject;

import java.awt.event.ActionEvent;

public class NewGameAction extends AbstractAction
        implements GameOverObserver, NewGameObserver {

    @Inject
    private Controller controller;

    @Inject
    private GameOverInformer gameOverInformer;

    @Inject
    private NewGameInformer newGameInformer;

    @Override
    public void init() {
        gameOverInformer.addGameOverObserver(this);
        newGameInformer.addNewGameObserver(this);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.newGame();
    }

    @Override
    public void gameOver() {
        setEnabled(true);
    }

    @Override
    public void newGameStarted() {
        setEnabled(false);
    }
}
