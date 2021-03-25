package amylopez.makelines.score;

import amylopez.makelines.score.impl.ScoreKeeperImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(ScoreKeeperImpl.class)
public interface ScoreKeeper {

    void init();

    void addObserver(ScoreObserver observer);

    int getLevel();

    int getRows();

    int getScore();

    void clearedRows(int numberOfRows);

    void clearedEntireBoard();

    void fastDrop(int dropHeight);

    void reset();
}
