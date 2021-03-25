package amylopez.makelines.score;

import java.util.Map;

public interface ScoreObserver {

    void scoreUpdate(Map<String, Integer> scoreInfo);
}
