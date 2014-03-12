package com.mm.tetris.controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.mm.tetris.audio.AudioPlayer;
import com.mm.tetris.board.BlockBoard;
import com.mm.tetris.board.BlockBoard.TetrominoDropResult;
import com.mm.tetris.controller.input.InputController;
import com.mm.tetris.gui.MessagePanel;
import com.mm.tetris.gui.Paintable;
import com.mm.tetris.gui.ScoreBoardView;
import com.mm.tetris.score.ScoreKeeper;
import com.mm.tetris.score.ScoreObserver;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Singleton
public class MainController implements
        Controller,
        ScoreObserver,
        NewTetrominoCreator,
        GameOverInformer,
        NewGameInformer {

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

    @Inject
    private MessagePanel messagePanel;

    @Inject
    private AudioPlayer audioPlayer;

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
     * Keeps track of how many rows the player manually dropped the current tetromino
     */
    private int rowsManuallyDropped = 0;

    /**
     * Has this class and all of its subclasses been initialized?
     */
    private boolean initialized = false;

    /**
     * List of observers that want to be informed when a new tetromino is created
     */
    private List<NewTetrominoObserver> newTetrominoObservers = new LinkedList<>();

    /**
     * List of observers that want to be informed when the game is over
     */
    private List<GameOverObserver> gameOverObservers = new LinkedList<>();

    /**
     * List of observers that want to be informed when a new game is started
     */
    private List<NewGameObserver> newGameObservers = new LinkedList<>();

    /**
	 * Constructor
	 */
	public MainController() {
		// do nothing
	}

    /**
     * Initialize this class
     */
    private void init() {
        initialized = true;

        loadConfigurationSettings();

        scoreKeeper.init();

        scoreKeeper.addObserver(this);

        audioPlayer.init();
    }

    @Override
	public void newGame() {
        log.debug("Starting new game");

        if (!initialized)
            init();

        audioPlayer.play();

        // reset
        scoreKeeper.reset();
        scoreBoardView.setVisible(false);
        messagePanel.hideMessage();

        // start the game
        synchronized (blockBoardLock) {
            blockBoard.start();
        }

        ticker.setTickListener(this).setInterval(levelFallingSpeeds.get(scoreKeeper.getLevel())).start();

        inputController.init();

        informNewGameObservers();

        entireWindow.repaint();
    }

    /**
     * Set the state of the game to Game Over
     */
    public void gameOver() {
        ticker.stop();
        audioPlayer.stop();
        informGameOverObservers();
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
     * @return TetrominoDropResult if the tetromino was dropped or set in place
     */
    private TetrominoDropResult dropTetrominoDownOneRow() {
        boolean shouldRepaintWholeWindow = false;
        TetrominoDropResult result;
        synchronized (blockBoardLock) {
            result = blockBoard.dropOneRow();
            switch (result) {
                case DROPPED:
                    break;

                case SET:
                    List<Integer> clearedRows = blockBoard.getCompletedRows();

                    // clear rows if necessary
                    if (!clearedRows.isEmpty()) {
                        blockBoard.clearRows(clearedRows);

                        // special scoring situation
                        if (blockBoard.wasBottomRowCleared(clearedRows)
                                && blockBoard.isEntireBoardClear())
                            scoreKeeper.clearedEntireBoard();

                        scoreKeeper.clearedRows(clearedRows.size());
                    }

                    // determine if we can continue playing
                    if (!blockBoard.isAbleToCreateNewTetromino()) {
                        gameOver();
                    } else {
                        blockBoard.loadNextTetromino();
                    }

                    // give player points for dropping the piece faster
                    scoreKeeper.fastDrop(rowsManuallyDropped);
                    rowsManuallyDropped = 0;

                    informNewTetrominoObservers();

                    shouldRepaintWholeWindow = true;
                    skipATick = true;
                    break;
            }
        }

        // only paint what we need to
        if (shouldRepaintWholeWindow) {
            entireWindow.repaint();
        } else {
            boardPanel.repaint();
        }

        return result;
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
        rowsManuallyDropped++;
    }

    /**
     * Pause the game
     */
    @Override
    public void pauseGame() {
        if (ticker.isRunning()) {
            ticker.stop();
            audioPlayer.pause();
        } else {
            ticker.start();
            audioPlayer.play();
        }
    }

    /**
     * Moves the current tetromino all the way to the bottom
     */
    @Override
    public void moveDownCompletely() {
        TetrominoDropResult result;

        // keep dropping until tetromino is set
        do {
            result = dropTetrominoDownOneRow();
            rowsManuallyDropped++;
        } while (result == TetrominoDropResult.DROPPED);

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

    /**
     * Add a new tetromino observer
     * @param observer NewTetrominoObserver
     */
    @Override
    public void addNewTetrominoObserver(NewTetrominoObserver observer) {
        newTetrominoObservers.add(observer);
    }

    /**
     * Inform observers a new tetromino was created
     */
    private void informNewTetrominoObservers() {
        for (NewTetrominoObserver observer : newTetrominoObservers) {
            observer.newTetrominoCreated();
        }
    }

    /**
     * Add a game over observer
     * @param observer GameOverObserver
     */
    @Override
    public void addGameOverObserver(GameOverObserver observer) {
        gameOverObservers.add(observer);
    }

    /**
     * Inform observers the game is over
     */
    private void informGameOverObservers() {
        for (GameOverObserver observer : gameOverObservers) {
            observer.gameOver();
        }
    }

    /**
     * Add a new game observer
     * @param observer NewGameObserver
     */
    @Override
    public void addNewGameObserver(NewGameObserver observer) {
        newGameObservers.add(observer);
    }

    /**
     * Inform observers that a new game has started
     */
    private void informNewGameObservers() {
        for (NewGameObserver observer : newGameObservers) {
            observer.newGameStarted();
        }
    }
}
