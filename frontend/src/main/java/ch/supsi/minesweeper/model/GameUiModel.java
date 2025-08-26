package ch.supsi.minesweeper.model;

import ch.supsi.minesweeper.service.GameService;


public class GameUiModel extends AbstractModel {

    private final GameService service;

    public GameUiModel(GameService service) {
        this.service = service;
    }

    public int getRows() { return service.getRows(); }
    public int getCols() { return service.getCols(); }
    public int getMines() { return service.getMines(); }
    public boolean isStarted() { return service.isStarted(); }
    public boolean isRevealed(int r, int c) { return service.isRevealed(r, c); }
    public boolean isFlagged(int r, int c) { return service.isFlagged(r, c); }
    public boolean hasMineAt(int r, int c) { return service.hasMineAt(r, c); }
    public int getNeighborCountAt(int r, int c) { return service.getNeighborCountAt(r, c); }
    public int getFlaggedCount() { return service.getFlaggedCount(); }
}