package ch.supsi.minesweeper.model;

import java.util.Random;

public class GameModel extends AbstractModel
        implements GameEventHandler, PlayerEventHandler {

    private static GameModel myself;

    private final int rows  = 9;
    private final int cols  = 9;
    private int       mines = 10;

    private boolean[][] hasMine;
    private int[][]     neighborCount;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private int         revealedCount;

    private GameModel() {
        super();
        initField();
    }

    public static GameModel getInstance() {
        if (myself == null) {
            myself = new GameModel();
        }
        return myself;
    }

    // --- proprietà dinamiche ---

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        if (mines < 1 || mines >= rows * cols) {
            throw new IllegalArgumentException("Numero di mine invalido: " + mines);
        }
        this.mines = mines;
    }

    // --- inizializzazione campo ---

    private void initField() {
        hasMine       = new boolean[rows][cols];
        neighborCount = new int[rows][cols];
        revealed      = new boolean[rows][cols];
        flagged       = new boolean[rows][cols];
        revealedCount = 0;
    }

    @Override
    public void newGame() {
        initField();
        generateField();
    }

    private void generateField() {
        // reset di tutte le proprietà
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                hasMine[r][c]       = false;
                neighborCount[r][c] = 0;
                revealed[r][c]      = false;
                flagged[r][c]       = false;
            }
        }
        // piazza le mine
        Random rnd = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if (!hasMine[r][c]) {
                hasMine[r][c] = true;
                placed++;
            }
        }
        // calcola neighbor count
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (hasMine[r][c]) continue;
                int cnt = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && hasMine[nr][nc]) {
                            cnt++;
                        }
                    }
                }
                neighborCount[r][c] = cnt;
            }
        }
    }

    // --- rivelazione e flag ---

    /**
     * Rivela la cella:
     * @return -1 se mina, altrimenti conteggio mine adiacenti
     */
    public int revealCell(int r, int c) {
        if (revealed[r][c]) {
            return neighborCount[r][c];
        }
        revealed[r][c] = true;
        if (hasMine[r][c]) {
            return -1;
        }
        revealedCount++;
        return neighborCount[r][c];
    }

    public void toggleFlag(int r, int c) {
        if (!revealed[r][c]) {
            flagged[r][c] = !flagged[r][c];
        }
    }

    public boolean isFlagged(int r, int c) {
        return flagged[r][c];
    }

    public boolean isWin() {
        return revealedCount == (rows * cols - mines);
    }

    public boolean hasMineAt(int r, int c) {
        return hasMine[r][c];
    }

    public int getNeighborCountAt(int r, int c) {
        return neighborCount[r][c];
    }

    // --- stub interfacce ---
    @Override public void save()   { /* TODO: persistenza */ }
    @Override public void move()   { /* gestito dal controller */ }
    @Override public void help()   { /* no-op */ }
    @Override public void about()  { /* no-op */ }
    @Override public void win()    { /* no-op */ }
    @Override public void lose()   { /* no-op */ }
}