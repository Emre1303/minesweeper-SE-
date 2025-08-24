package ch.supsi.minesweeper.controller;

public interface PlayerEventHandler {

    void move();
    void reveal(int r, int c);
    void toggleFlag(int r, int c);
}