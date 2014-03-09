package com.mm.tetris.controller.input.action;

import com.mm.tetris.controller.Controller;
import com.mm.tetris.controller.input.action.AbstractAction;

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
