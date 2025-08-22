package ch.supsi.minesweeper.model;

import java.util.*;

public class GameModel extends AbstractModel
        implements GameEventHandler, PlayerEventHandler {

    private static GameModel myself;
    private final int rows  = 9;
    private final int cols  = 9;
    private int mines = 10;
    private boolean started = false;
    private final Grid g;

    private GameModel() {
        super();
        this.g = new Grid(rows, cols);
        g.clear();
    }

    public static GameModel getInstance() {
        if (myself == null) {
            myself = new GameModel();
        }
        return myself;
    }

    public int getRows()     { return rows; }
    public int getCols()     { return cols; }
    public int getMines()    { return mines; }

    public void setMines(int mines) {
        if (mines < 1 || mines >= rows * cols) {
            throw new IllegalArgumentException("Numero di mine invalido: " + mines);
        }
        this.mines = mines;
    }

    public boolean isStarted()            { return started; }
    public boolean isRevealed(int r, int c) { return g.revealed[r][c]; }
    public boolean hasMineAt(int r, int c)  { return g.hasMine[r][c]; }
    public boolean isFlagged(int r, int c)  { return g.flagged[r][c]; }
    public int getNeighborCountAt(int r, int c) { return g.neighborCount[r][c]; }
    public int getFlaggedCount() { return g.getFlaggedCount(); }

   /* private void initField() {
        hasMine       = new boolean[rows][cols];
        neighborCount = new int[rows][cols];
        revealed      = new boolean[rows][cols];
        flagged       = new boolean[rows][cols];
        revealedCount = 0;
    }*/

    @Override
    public void newGame() {
        FieldGenerator.generate(g, mines);
        started = true;
    }



    public List<int[]> revealArea(int r, int c) {
        return AreaRevealer.revealArea(g, r, c);
    }


    public void toggleFlag(int r, int c) {
        if (!g.revealed[r][c]) {
            g.flagged[r][c] = !g.flagged[r][c];
        }
    }

    public boolean isWin() {
        return g.revealedCount == (rows * cols - mines);
    }

    public void loadFromState(GameStateJson state) {
        this.mines = state.getMines();
        StateLoader.load(g, state);
    }

    public void markStarted() {
        this.started = true;
    }

    @Override
    public void move() {
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException(
                "Usa JsonGamePersistence o un altro GamePersistence per salvare."
        );
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException(
                "Usa JsonGamePersistence o un altro GamePersistence per caricare."
        );
    }

    @Override
    public void help() {
    }

    @Override
    public void about() {
    }

    @Override
    public void win() {
    }

    @Override
    public void lose() {
    }
}