package amylopez.makelines.controller;

import com.google.inject.ImplementedBy;

/**
 * An interface for classes that create new tetrominos
 */
@ImplementedBy(MainController.class)
public interface NewTetrominoCreator {

    void addNewTetrominoObserver(NewTetrominoObserver observer);
}
