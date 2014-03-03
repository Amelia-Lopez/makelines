package com.mm.tetris.action;

import com.mm.tetris.controller.Controller;

import javax.inject.Inject;

import java.awt.event.ActionEvent;

public class NewGameAction extends AbstractAction {

    @Inject
    private Controller controller;

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.newGame();
    }
}
