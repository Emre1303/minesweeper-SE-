package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface GameEventHandler extends EventHandler {
    void newGame();
    void save();
    void help();
    void about();
}