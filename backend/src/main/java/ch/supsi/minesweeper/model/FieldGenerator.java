package ch.supsi.minesweeper.model;

import java.util.Random;

final class FieldGenerator {

    static void generate(Grid g, int mines) {
        g.clear();
        Random rnd = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rnd.nextInt(g.rows), c = rnd.nextInt(g.cols);
            if (!g.hasMine[r][c]) { g.hasMine[r][c] = true; placed++; }
        }
        for (int r=0; r < g.rows ; r++) for (int c=0 ;c < g.cols ; c++) {
            if (g.hasMine[r][c]) { g.neighborCount[r][c]=0; continue; }
            int cnt=0;
            for (int dr=-1; dr<=1; dr++) for (int dc=-1; dc<=1; dc++) {
                if (dr==0 && dc==0) continue;
                int nr=r+dr, nc=c+dc;
                if (g.inBounds(nr,nc) && g.hasMine[nr][nc]) cnt++;
            }
            g.neighborCount[r][c]=cnt;
        }
    }
}