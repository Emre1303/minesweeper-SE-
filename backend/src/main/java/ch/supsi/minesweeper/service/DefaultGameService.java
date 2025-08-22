package ch.supsi.minesweeper.service;

import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.service.GameRepository;

import java.nio.file.Path;
import java.util.List;

public class DefaultGameService implements GameService {

    private final GameModel model;
    private final GameRepository repo;

    public DefaultGameService(GameModel model, GameRepository repo) {
        this.model = model;
        this.repo = repo;
    }

    @Override public void newGame() { model.newGame(); }
    @Override public List<int[]> revealArea(int r,int c){ return model.revealArea(r,c); }
    @Override public void toggleFlag(int r,int c){ model.toggleFlag(r,c); }
    @Override public boolean isWin(){ return model.isWin(); }

    @Override public void save(Path path){ repo.save(model, path); }
    @Override public void load(Path path){ repo.load(model, path); }

    @Override public int getRows(){ return model.getRows(); }
    @Override public int getCols(){ return model.getCols(); }
    @Override public int getMines(){ return model.getMines(); }
    @Override public boolean isRevealed(int r,int c){ return model.isRevealed(r,c); }
    @Override public boolean isFlagged(int r,int c){ return model.isFlagged(r,c); }
    @Override public boolean hasMineAt(int r,int c){ return model.hasMineAt(r,c); }
    @Override public int getNeighborCountAt(int r,int c){ return model.getNeighborCountAt(r,c); }
    @Override public int getFlaggedCount(){ return model.getFlaggedCount(); }
}