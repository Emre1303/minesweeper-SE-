package ch.supsi.minesweeper.model;

final class StateLoader {

    static void load(Grid g, GameStateJson state) {
        g.clear();
        for(int r=0; r < g.rows; r++) {
            for(int c=0; c < g.cols; c++) {
                g.hasMine[r][c] = state.getHasMine()[r][c];
                g.revealed[r][c] = state.getRevealed()[r][c];
                g.flagged[r][c] = state.getFlagged()[r][c];
            }
        }
        for(int r = 0 ; r<g.rows; r++) {
            for(int c=0 ; c<g.cols; c++){
                if (g.hasMine[r][c]) { g.neighborCount[r][c]=0; continue; }
                int cnt=0;
                for(int dr=-1; dr<=1; dr++) for(int dc=-1; dc<=1; dc++){
                    if (dr==0 && dc==0) continue;
                    int nr=r+dr, nc=c+dc;
                    if (g.inBounds(nr,nc) && g.hasMine[nr][nc]) cnt++;
                }
                g.neighborCount[r][c]=cnt;
            }
        }
        g.revealedCount = 0;
        for(int r=0;r<g.rows;r++) {
            for(int c=0;c<g.cols;c++)
                if (g.revealed[r][c] && !g.hasMine[r][c]) g.revealedCount++;
        }
    }
}