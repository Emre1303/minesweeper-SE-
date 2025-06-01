package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.controller.EventHandler;

public interface GameEventHandler extends EventHandler {
    void newGame();
    void save();
    void load();     // ← deve esserci
    void help();
    void about();
    void win();
    void lose();
}