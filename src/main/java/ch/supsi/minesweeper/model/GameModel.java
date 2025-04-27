package ch.supsi.minesweeper.model;

import java.util.Random;

public class GameModel extends AbstractModel
        implements GameEventHandler, PlayerEventHandler {

    private static GameModel myself;

    // dimensioni e numero di mine (puoi cambiare questi valori)
    private final int rows  = 9;
    private final int cols  = 9;
    private final int mines = 10;

    // stato del campo
    private boolean[][] hasMine;
    private int[][]     neighborCount;

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

    /** Inizializza le matrici interne. */
    private void initField() {
        hasMine       = new boolean[rows][cols];
        neighborCount = new int[rows][cols];
    }

    /** Chiamato da GameController.newGame(): genera un nuovo campo. */
    @Override
    public void newGame() {
        generateField();
    }

    /** 1) resetta, 2) piazza mine, 3) calcola i conteggi adiacenti. */
    private void generateField() {
        // 1) reset
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                hasMine[r][c]       = false;
                neighborCount[r][c] = 0;
            }
        }

        // 2) piazza mine in posizioni uniche
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

        // 3) calcola neighborCount per ogni cella sicura
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (hasMine[r][c]) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && hasMine[nr][nc]) {
                            count++;
                        }
                    }
                }
                neighborCount[r][c] = count;
            }
        }
    }






    @Override public void save()   { /* TODO */ }
    @Override public void move()   { /* gestito dal controller */ }
    @Override public void help()   { /* no-op */ }
    @Override public void about()  { /* no-op */ }

}