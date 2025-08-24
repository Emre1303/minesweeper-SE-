package ch.supsi.minesweeper.model;


public class GameModel extends AbstractModel {

    private static GameModel myself;

    private final int rows  = 9;
    private final int cols  = 9;

    private int  mines   = 10;
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


    public int getRows()  { return rows; }
    public int getCols()  { return cols; }
    public int getMines() { return mines; }

    public boolean isStarted() { return started; }

    public boolean isRevealed(int r, int c)     { return g.revealed[r][c]; }
    public boolean isFlagged(int r, int c)      { return g.flagged[r][c]; }
    public boolean hasMineAt(int r, int c)      { return g.hasMine[r][c]; }
    public int  getNeighborCountAt(int r, int c){ return g.neighborCount[r][c]; }

    public int getFlaggedCount() {
        return g.getFlaggedCount();
    }

    public int getRevealedCount() { return g.revealedCount; }



    public void setMines(int mines) {
        if (mines < 1 || mines >= rows * cols) {
            throw new IllegalArgumentException("Numero di mine invalido: " + mines);
        }
        this.mines = mines;
    }

    public void setStarted(boolean started) { this.started = started; }

    public void clearGrid() {
        g.clear();
        g.revealedCount = 0;
    }

    public void setHasMineAt(int r, int c, boolean v)     { g.hasMine[r][c] = v; }
    public void setNeighborCountAt(int r, int c, int v)   { g.neighborCount[r][c] = v; }
    public void setRevealedAt(int r, int c, boolean v)    { g.revealed[r][c] = v; }
    public void setFlagAt(int r, int c, boolean v) {
        if (!g.revealed[r][c]) { g.flagged[r][c] = v; }
    }
    public void incrementRevealedCount(int delta)         { g.revealedCount += delta; }
    public void setRevealedCount(int value)               { g.revealedCount = value; }


    public void loadFromState(GameStateJson state) {
        this.mines = state.getMines();
        StateLoader.load(g, state);
    }

    public void markStarted() { this.started = true; }


}