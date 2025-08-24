package ch.supsi.minesweeper.controller;

public interface GameEventHandler{
    void newGame();
    void save();
    void load();     // ← deve esserci
    void help();
    void about();
    void win();
    void lose();
}