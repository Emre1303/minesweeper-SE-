package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.service.GameService;
import ch.supsi.minesweeper.view.DataView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;
import java.util.ResourceBundle;

public class GameController implements EventHandler {

    private static GameController myself;

    private final GameModel gameModel;
    private GameService gameService;

    private MenuController menu;
    private List<DataView> views;

    private GameController() {
        this.gameModel = GameModel.getInstance();
    }

    public static GameController getInstance() {
        if (myself == null) myself = new GameController();
        return myself;
    }

    public void setGameService(GameService gameService) { this.gameService = gameService; }

    public void attachMenuController(ResourceBundle bundle, int defaultBombs) {
        this.menu = new MenuController(gameService, gameModel, bundle, defaultBombs);
        if (views != null) this.menu.initialize(views);
    }

    public void initialize(List<DataView> views) {
        this.views = views;
        if (this.menu != null) this.menu.initialize(views);
    }

    public void reveal(int r, int c) {
        gameService.revealArea(r, c);
        if (views != null) views.forEach(DataView::update);
        if (gameService.isWin()) win();
    }

    public void toggleFlag(int r, int c) {
        gameService.toggleFlag(r, c);
        if (views != null) views.forEach(DataView::update);
    }

    @Override public void move() { gameModel.move(); }

    @Override public void newGame()      { menu.newGame(); }
    @Override public void save()         { menu.save(); }
    public  void saveAs()                { menu.saveAs(); }
    @Override public void load()         { menu.load(); }
    public  void open()                  { menu.open(); }
    @Override public void help()         { menu.help(); }
    @Override public void about()        { menu.about(); }
    @Override public void win()          { menu.win(); }
    @Override public void lose()         { menu.lose(); }

    private void toast(String msg){
        Platform.runLater(() -> new Alert(AlertType.INFORMATION, msg).showAndWait());
    }
}