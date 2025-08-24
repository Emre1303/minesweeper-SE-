package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.service.GameService;
import ch.supsi.minesweeper.view.DataView;

import java.util.List;
import java.util.ResourceBundle;

public class GameController implements EventHandler {

    private static GameController myself;

    private GameService gameService;

    private MenuController menu;
    private List<DataView> views;

    private GameController() {
    }
    @Override
    public void reveal(int r, int c) {
        gameService.revealArea(r, c);

        if (views != null) views.forEach(DataView::update);

        if (gameService.hasMineAt(r, c)) {
            gameService.revealAllMines();
            if (views != null) views.forEach(DataView::update);

            gameService.endGame();
            lose();
            if (views != null) views.forEach(DataView::update);

        } else if (gameService.isWin()) {
            gameService.endGame();
            if (views != null) views.forEach(DataView::update);

            win();

        }

    }
    @Override
    public void toggleFlag(int r, int c) {
        gameService.toggleFlag(r, c);
        if (views != null) views.forEach(DataView::update);
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public static GameController getInstance() {
        if (myself == null) myself = new GameController();
        return myself;
    }


    public void attachMenuController(ResourceBundle bundle, int defaultBombs) {
        this.menu = new MenuController(gameService, bundle, defaultBombs);
        if (views != null) this.menu.initialize(views);
    }

    public void initialize(List<DataView> views) {
        this.views = views;
        if (this.menu != null) this.menu.initialize(views);
    }


    @Override public void move() {}

    @Override public void newGame()      { menu.newGame(); }
    @Override public void save()         { menu.save(); }
    public  void saveAs()                { menu.saveAs(); }
    @Override public void load()         { menu.load(); }
    public  void open()                  { menu.open(); }
    @Override public void help()         { menu.help(); }
    @Override public void about()        { menu.about(); }
    @Override public void win()          { menu.win(); }
    @Override public void lose()         { menu.lose(); }

}