package com.mariolopezjr.tetris.controller.input.action;

import java.awt.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuitAction extends AbstractAction {

    private static final long serialVersionUID = 5598230955495706397L;

    private static Logger log = LoggerFactory.getLogger(QuitAction.class);

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("Application exited normally");
        System.exit(0);
    }
}
