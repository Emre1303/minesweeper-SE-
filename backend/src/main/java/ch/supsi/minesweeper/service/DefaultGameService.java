package ch.supsi.minesweeper.service;

import ch.supsi.minesweeper.model.GameModel;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultGameService implements GameService {

    private final GameModel model;
    private final GameRepository repo;

    public DefaultGameService(GameModel model, GameRepository repo) {
        this.model = model;
        this.repo  = repo;
    }


    @Override
    public void newGame() {
        model.clearGrid();

        int rows = model.getRows(), cols = model.getCols();
        int mines = model.getMines();
        Random rnd = new Random();

        int placed = 0;
        while (placed < mines) {
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if (!model.hasMineAt(r, c)) {
                model.setHasMineAt(r, c, true);
                placed++;
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (model.hasMineAt(r, c)) {
                    model.setNeighborCountAt(r, c, 0);
                    continue;
                }
                int cnt = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        if (dr == 0 && dc == 0) continue;
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && model.hasMineAt(nr, nc)) {
                            cnt++;
                        }
                    }
                }
                model.setNeighborCountAt(r, c, cnt);
            }
        }

        model.setStarted(true);
    }

    @Override
    public void endGame() {
        model.setStarted(false);
    }

    @Override
    public List<int[]> revealArea(int r, int c) {
        List<int[]> opened = new ArrayList<>();
        if (model.isRevealed(r, c) || model.isFlagged(r, c)) return opened;

        int rows = model.getRows(), cols = model.getCols();

        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{r, c});
        model.setRevealedAt(r, c, true);

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            int row = pos[0], col = pos[1];
            opened.add(pos);

            if (model.hasMineAt(row, col)) continue;
            if (model.getNeighborCountAt(row, col) != 0) continue;

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = row + dr, nc = col + dc;
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    if (!model.isRevealed(nr, nc) && !model.isFlagged(nr, nc)) {
                        model.setRevealedAt(nr, nc, true);
                        q.add(new int[]{nr, nc});
                    }
                }
            }
        }

        model.incrementRevealedCount(opened.size());
        return opened;
    }

    @Override
    public void toggleFlag(int r, int c) {
        if (!model.isRevealed(r, c)) {
            boolean now = model.isFlagged(r, c);
            model.setFlagAt(r, c, !now);
        }
    }
    @Override
    public List<int[]> revealAllMines() {
        List<int[]> mines = new ArrayList<>();
        int rows = model.getRows();
        int cols = model.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (model.hasMineAt(r, c) && !model.isRevealed(r, c)) {
                    model.setRevealedAt(r, c, true);
                    mines.add(new int[]{r, c});
                }
            }
        }
        return mines;
    }

    @Override
    public boolean isWin() {
        return model.getRevealedCount() == (model.getRows() * model.getCols() - model.getMines());
    }

    @Override
    public boolean isStarted() {
        return model.isStarted();
    }

    @Override public void setMines(int mines) { model.setMines(mines); }


    @Override public void save(Path path) { repo.save(model, path); }
    @Override public void load(Path path) { repo.load(model, path); }


    @Override public int  getRows()                       { return model.getRows(); }
    @Override public int  getCols()                       { return model.getCols(); }
    @Override public int  getMines()                      { return model.getMines(); }
    @Override public boolean isRevealed(int r,int c)      { return model.isRevealed(r,c); }
    @Override public boolean isFlagged(int r,int c)       { return model.isFlagged(r,c); }
    @Override public boolean hasMineAt(int r,int c)       { return model.hasMineAt(r,c); }
    @Override public int  getNeighborCountAt(int r,int c) { return model.getNeighborCountAt(r,c); }
    @Override public int  getFlaggedCount()               { return model.getFlaggedCount(); }
}