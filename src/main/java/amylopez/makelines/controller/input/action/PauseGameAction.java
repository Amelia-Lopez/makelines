package amylopez.makelines.controller.input.action;

import amylopez.makelines.controller.GameOverInformer;
import amylopez.makelines.controller.GameOverObserver;
import amylopez.makelines.controller.NewGameInformer;
import amylopez.makelines.controller.NewGameObserver;
import amylopez.makelines.controller.input.InputController;
import amylopez.makelines.gui.MessagePanel;

import javax.inject.Inject;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Action that pauses the game
 */
public class PauseGameAction extends AbstractAction
        implements GameOverObserver, ImageMessager, NewGameObserver {

    @Inject
    private InputController inputController;

    @Inject
    private GameOverInformer gameOverInformer;

    @Inject
    private NewGameInformer newGameInformer;

    @Inject
    private MessagePanel messagePanel;

    // image that says "Paused"
    private BufferedImage image;

    private boolean isPaused = false;

    @Override
    public void init() {
        gameOverInformer.addGameOverObserver(this);
        newGameInformer.addNewGameObserver(this);
        setEnabled(false);
    }

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

    @Override
    public void gameOver() {
        setEnabled(false);
    }

    @Override
    public void newGameStarted() {
        setEnabled(true);
    }
}
