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

    /** Restituisce il numero di righe */
    public int getRows() {
        return rows;
    }

    /** Restituisce il numero di colonne */
    public int getCols() {
        return cols;
    }

    /** Restituisce il numero di mine impostato */
    public int getMines() {
        return mines;
    }

    /** Imposta il numero di mine per la prossima partita */
    public void setMines(int mines) {
        if (mines < 1 || mines >= rows * cols) {
            throw new IllegalArgumentException("Numero di mine invalido: " + mines);
        }
        this.mines = mines;
    }

    /** Conta quante bandiere sono state piazzate */
    public int getFlaggedCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (flagged[r][c]) count++;
            }
        }
        return count;
    }

    /** Inizializza tutte le strutture dati per una nuova partita */
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

    /** Genera il campo: piazza random mine e calcola i contatori */
    private void generateField() {
        // reset
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                hasMine[r][c]       = false;
                neighborCount[r][c] = 0;
                revealed[r][c]      = false;
                flagged[r][c]       = false;
            }
        }
        // piazza mine
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
        // calcola conteggi
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

    /** Rivela una cella: ritorna -1 se mina, altrimenti numero di mine adiacenti */
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

    /** Toggle bandiera sulla cella, se non rivelata */
    public void toggleFlag(int r, int c) {
        if (!revealed[r][c]) {
            flagged[r][c] = !flagged[r][c];
        }
    }

    public boolean isFlagged(int r, int c) {
        return flagged[r][c];
    }

    /** True se tutte le celle non mina sono rivelate */
    public boolean isWin() {
        return revealedCount == (rows * cols - mines);
    }

    public boolean hasMineAt(int r, int c) {
        return hasMine[r][c];
    }

    public int getNeighborCountAt(int r, int c) {
        return neighborCount[r][c];
    }

    // -- stub interfacce --
    @Override public void save()   { }
    @Override public void move()   { }
    @Override public void help()   { }
    @Override public void about()  { }
    @Override public void win()    { }
    @Override public void lose()   { }
}
