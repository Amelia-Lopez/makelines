package amylopez.makelines.controller;

import com.google.inject.ImplementedBy;

/**
 * The interface for the class that informs observers of a Game Over
 */
@ImplementedBy(MainController.class)
public interface GameOverInformer {

    void addGameOverObserver(GameOverObserver observer);
}
