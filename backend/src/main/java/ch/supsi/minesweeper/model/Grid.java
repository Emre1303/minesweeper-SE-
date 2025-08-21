package ch.supsi.minesweeper.model;

final class Grid {
    final int rows;
    final int cols;
    final boolean[][] hasMine;
    final int[][]     neighborCount;
    final boolean[][] revealed;
    final boolean[][] flagged;
    int revealedCount = 0;

    Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.hasMine = new boolean[rows][cols];
        this.neighborCount = new int[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.flagged = new boolean[rows][cols];
    }

    void clear() {
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) {
            hasMine[r][c]=false; neighborCount[r][c]=0; revealed[r][c]=false; flagged[r][c]=false;
        }
        revealedCount = 0;
    }
    //Ritorna true se il punto si trova all'interno del griglia
    boolean inBounds(int r,int c){ return r >= 0 && r < rows && c >= 0 && c < cols; }

    int getFlaggedCount(){ int cnt=0; for(int r=0;r<rows;r++) for(int c=0;c<cols;c++) if(flagged[r][c]) cnt++; return cnt; }
}