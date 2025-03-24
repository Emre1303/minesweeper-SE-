package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;

import java.util.List;

public class GameController implements GameEventHandler, PlayerEventHandler {

    private static GameController myself;

    private GameModel gameModel;

    private List<DataView> views;

    private GameController () {
        this.gameModel = GameModel.getInstance();
    }

    public static GameController getInstance() {
        if (myself == null) {
            myself = new GameController();
        }

        return myself;
    }

    public void initialize(List<DataView> views) {
        this.views = views;
    }

    @Override
    public void newGame() {
        // do whatever you must do to start a new game

        // then update your views
        this.views.forEach(DataView::update);
    }

    @Override
    public void save() {
        // do whatever you must do to start a new game

        // then update your views
        this.views.forEach(DataView::update);
    }

    // add all the relevant methods to handle all those defined by the GameEventHandler interface
    // ...

    @Override
    public void move() {
        this.gameModel.move();
        views.forEach(DataView::update);
    }

}
