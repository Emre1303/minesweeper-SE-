package ch.supsi.minesweeper.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
    private boolean started = false;

    public int getNeighborCountAt(int r, int c) {
        return neighborCount[r][c];
    }

    public List<int[]> revealArea(int r, int c) {
        List<int[]> opened = new ArrayList<>();
        if (revealed[r][c] || flagged[r][c]) return opened;

        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{r, c});
        revealed[r][c] = true;

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            int row = pos[0], col = pos[1];
            opened.add(pos);

            if (hasMine[row][col]) continue;
            if (neighborCount[row][col] != 0) continue;

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int nr = row + dr, nc = col + dc;
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    if (!revealed[nr][nc] && !flagged[nr][nc]) {
                        revealed[nr][nc] = true;
                        q.add(new int[]{nr, nc});
                    }
                }
            }
        }

        revealedCount += opened.size();
        return opened;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isRevealed(int r, int c) {
        return revealed[r][c];
    }

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

    public int getFlaggedCount() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (flagged[r][c]) count++;
            }
        }
        return count;
    }

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
        started = true;
    }

    public void reset() {
        started = false;
    }

    private void generateField() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                hasMine[r][c]       = false;
                neighborCount[r][c] = 0;
                revealed[r][c]      = false;
                flagged[r][c]       = false;
            }
        }
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

    public void saveToJson(Path path) throws IOException {
        GameStateJson state = new GameStateJson(this);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(path.toFile(), state);
    }

    public void loadFromJson(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GameStateJson state = mapper.readValue(path.toFile(), GameStateJson.class);

        this.mines   = state.getMines();
        this.started = true;

        initField();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.hasMine[r][c]   = state.getHasMine()[r][c];
                this.revealed[r][c]  = state.getRevealed()[r][c];
                this.flagged[r][c]   = state.getFlagged()[r][c];
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (hasMine[r][c]) {
                    neighborCount[r][c] = 0;
                    continue;
                }
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

        revealedCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (revealed[r][c] && !hasMine[r][c]) {
                    revealedCount++;
                }
            }
        }
    }

    @Override
    public void save() {
        try {
            saveToJson(Path.of("gamestate.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            loadFromJson(Path.of("gamestate.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void move() { }

    @Override
    public void help() { }

    @Override
    public void about() {}

    @Override
    public void win() {}

    @Override
    public void lose() {}
}