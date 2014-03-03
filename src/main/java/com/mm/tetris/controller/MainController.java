package com.mm.tetris.controller;

import javax.inject.Inject;
import javax.inject.Named;

import com.mm.tetris.board.BlockBoard;
import com.mm.tetris.board.BlockBoard.TetrominoDropResult;
import com.mm.tetris.gui.Paintable;
import com.mm.tetris.gui.ScoreBoardView;
import com.mm.tetris.score.ScoreKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
	
	private boolean skipATick = false;
	
	
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
        blockBoard.start();
        ticker.setTickListener(this).setInterval(100).start();

        entireWindow.repaint();
    }

	@Override
	public void tick() {
        TetrominoDropResult result = blockBoard.dropOneRow();
        switch (result) {
            case DROPPED:
                boardPanel.repaint();
                break;
            case SET:
                List<Integer> clearedRows = blockBoard.getCompletedRows();
                if (!clearedRows.isEmpty()) {
                    blockBoard.clearRows(clearedRows);
                    scoreKeeper.clearedRows(clearedRows.size());
                }
                blockBoard.loadNextTetromino();
                entireWindow.repaint();
                break;
        }
	}

}
