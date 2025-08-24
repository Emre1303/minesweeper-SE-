package ch.supsi.minesweeper.service;

import java.nio.file.Path;
import java.util.List;

public interface GameService {
    void newGame();
    List<int[]> revealArea(int r, int c);
    void toggleFlag(int r, int c);
    boolean isWin();

    void save(Path path);
    void load(Path path);

    void setMines(int mines);
    int getRows();
    int getCols();
    int getMines();
    boolean isRevealed(int r, int c);
    boolean isFlagged(int r, int c);
    boolean hasMineAt(int r, int c);
    int getNeighborCountAt(int r, int c);
    int getFlaggedCount();
    List<int[]> revealAllMines();
    boolean isStarted();
    void endGame();
}