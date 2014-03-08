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
import com.mm.tetris.score.ScoreObserver;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MainController implements Controller, ScoreObserver {

    private static Logger log = LoggerFactory.getLogger(MainController.class);

    @Inject
    private Configuration config;
	
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
     * The speed the tetromino should fall based on the level.  Values are configured.
     */
    private Map<Integer, Integer> levelFallingSpeeds;

    /**
     * Will skip a tick (which just drops the tetromino down one row) in case a particular
     * event occurs
     */
	private volatile boolean skipATick = false;

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

        loadConfigurationSettings();

        // reset
        scoreKeeper.reset();
        scoreBoardView.setVisible(false);

        // start the game
        synchronized (blockBoardLock) {
            blockBoard.start();
        }

        ticker.setTickListener(this).setInterval(levelFallingSpeeds.get(0)).start();

        inputController.init();

        entireWindow.repaint();
    }

    /**
     * Load the configuration settings required for the main controller
     */
    private void loadConfigurationSettings() {
        // how fast the tetromino should fall for each level
        levelFallingSpeeds = new HashMap<>();
        int fallingSpeedSize = config.getInt("movement/falling/@size");
        for (int current = 1; current <= fallingSpeedSize; current++) {
            String configPath = "movement/falling/level[" + current + "]/";
            levelFallingSpeeds.put(
                    config.getInt(configPath + "@value"),
                    config.getInt(configPath + "@delay"));
        }
    }

	@Override
	public void tick() {
        if (skipATick) {
            skipATick = false;
            return;
        }

        dropTetrominoDownOneRow();
	}

    /**
     * Drops the tetromino down one row and sets it in place if it can't
     */
    private void dropTetrominoDownOneRow() {
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

    /**
     * Manually move the tetromino down one row
     */
    @Override
    public void moveDownOneRow() {
        skipATick = true;
        dropTetrominoDownOneRow();
    }

    /**
     * Moves the current tetromino all the way to the bottom
     */
    @Override
    public void moveDownCompletely() {
        // todo: add support to move current tetromino down completely
    }

    @Override
    public void rotateClockwise() {
        blockBoard.rotateClockwise();
        boardPanel.repaint();
    }

    @Override
    public void rotateCounterClockwise() {
        blockBoard.rotateCounterClockwise();
        boardPanel.repaint();
    }

    @Override
    public void scoreUpdate(Map<String, Integer> scoreInfo) {
        int currentLevel = scoreInfo.get("Level");
        ticker.setInterval(levelFallingSpeeds.get(currentLevel));
    }
}
