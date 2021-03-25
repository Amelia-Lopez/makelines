package amylopez.makelines.controller;

import com.google.inject.ImplementedBy;

/**
 * An interface for the class that should inform the observers that a new game has started.
 */
@ImplementedBy(MainController.class)
public interface NewGameInformer {

    void addNewGameObserver(NewGameObserver observer);
}
