package com.mm.tetris.controller.input.action;

import com.mm.tetris.controller.input.InputController;
import com.mm.tetris.gui.MessagePanel;

import javax.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Action that pauses the game
 */
public class PauseGameAction extends AbstractAction implements ImageMessager {

    @Inject
    private InputController inputController;

    @Inject
    private MessagePanel messagePanel;

    private BufferedImage image;

    private boolean isPaused = false;

    @Override
    public void actionPerformed(ActionEvent e) {
        inputController.pause();

        if (isPaused) {
            isPaused = false;
            messagePanel.hideMessage();
        } else {
            isPaused = true;
            messagePanel.showMessage(image);
        }
    }

    @Override
    public void setMessageImage(BufferedImage image) {
        this.image = image;
    }
}
