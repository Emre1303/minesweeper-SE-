package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface GameEventHandler extends EventHandler {

    void newGame();

    void save();

    // add all the relevant missing behaviours
    // ...

}
