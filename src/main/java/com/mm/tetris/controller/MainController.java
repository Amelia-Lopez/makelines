package com.mm.tetris.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.mm.tetris.board.BlockBoard;
import com.mm.tetris.board.BlockBoard.TetrominoDropResult;
import com.mm.tetris.controller.input.InputController;
import com.mm.tetris.gui.Paintable;
import com.mm.tetris.gui.ScoreBoardView;
import com.mm.tetris.score.ScoreKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class MainController implements Controller {

    private static Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Inject
	private BlockBoard blockBoard;
	
	@Inject @Named("All")
	private Paintable entireWindow;
	
	@Inject @Named("Board")
	private Paintable boardPanel;
	
	@Inject
	private ScoreKeeper scoreKeeper;
	
	@Inject
	private ScoreBoardView scoreBoardView;
	
	@Inject
	private Ticker ticker;

    @Inject
    private InputController inputController;

    /**
     * Will skip a tick (which just drops the tetromino down one row) in case a particular
     * event occurs
     */
	private boolean skipATick = false;

    /**
     * Lock that should be synchronized whenever the blockBoard is being accessed
     */
    private final Object blockBoardLock = new Object();
	
	
	/**
	 * Constructor
	 */
	public MainController() {
		// do nothing
	}

	@Override
	public void newGame() {
        log.debug("Starting new game");

        // reset
        scoreKeeper.reset();
        scoreBoardView.setVisible(false);

        // start the game
        synchronized (blockBoardLock) {
            blockBoard.start();
        }
        // todo: configured dropping speed
        ticker.setTickListener(this).setInterval(300).start();

        inputController.init();

        entireWindow.repaint();
    }

	@Override
	public void tick() {
        if (skipATick) {
            skipATick = false;
            return;
        }

        boolean shouldRepaintWholeWindow = false;
        synchronized (blockBoardLock) {
            TetrominoDropResult result = blockBoard.dropOneRow();
            switch (result) {
                case DROPPED:
                    break;
                case SET:
                    List<Integer> clearedRows = blockBoard.getCompletedRows();
                    if (!clearedRows.isEmpty()) {
                        blockBoard.clearRows(clearedRows);
                        scoreKeeper.clearedRows(clearedRows.size());
                    }
                    blockBoard.loadNextTetromino();
                    shouldRepaintWholeWindow = true;
                    skipATick = true;
                    break;
            }
        }

        if (shouldRepaintWholeWindow) {
            entireWindow.repaint();
        } else {
            boardPanel.repaint();
        }
	}

    /**
     * Move the current tetromino to the left
     */
    public void moveLeft() {
        synchronized (blockBoardLock) {
            blockBoard.moveCurrentTetrominoLeft();
        }
        boardPanel.repaint();
    }

    /**
     * Move the current tetromino to the right
     */
    public void moveRight() {
        synchronized (blockBoardLock) {
            blockBoard.moveCurrentTetrominoRight();
        }
        boardPanel.repaint();
    }

}
