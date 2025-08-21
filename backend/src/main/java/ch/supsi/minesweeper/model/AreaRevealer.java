package ch.supsi.minesweeper.model;

import java.util.*;

final class AreaRevealer {

    static List<int[]> revealArea(Grid g, int r, int c) {
        List<int[]> opened = new ArrayList<>();
        if (g.revealed[r][c] || g.flagged[r][c]) return opened;

        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{r,c});
        g.revealed[r][c] = true;

        while(!queue.isEmpty()){
            int[] pos = queue.poll();
            int row=pos[0], col=pos[1];
            opened.add(pos);

            if (g.hasMine[row][col]) continue;
            if (g.neighborCount[row][col] != 0) continue;
            for(int dr=-1; dr<=1; dr++) for(int dc=-1; dc<=1; dc++){
                if (dr==0 && dc==0) continue;
                int nr=row+dr, nc=col+dc;
                if (!g.inBounds(nr,nc)) continue;
                if (!g.revealed[nr][nc] && !g.flagged[nr][nc]){
                    g.revealed[nr][nc]=true; queue.add(new int[]{nr,nc});
                }
            }
        }
        g.revealedCount += opened.size();
        return opened;
    }
}