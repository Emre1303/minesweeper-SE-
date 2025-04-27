package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class GameController implements GameEventHandler, PlayerEventHandler {

    private static GameController myself;
    private final GameModel gameModel;
    private List<DataView> views;

    private GameController() {
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
        gameModel.newGame();
        views.forEach(DataView::update);
    }

    @Override
    public void save() {
        gameModel.save();
        views.forEach(DataView::update);
    }

    @Override
    public void help() {
        Platform.runLater(() -> {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Help");
            a.setHeaderText("How to play");
            a.setContentText(
                    "• Left-click to reveal a cell\n" +
                            "• Right-click to flag/unflag\n" +
                            "• Reveal all safe cells to win.\n" +
                            "…"
            );
            a.showAndWait();
        });
    }

    @Override
    public void about() {
        Platform.runLater(() -> {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("About");
            a.setHeaderText("Minesweeper JavaFX");
            a.setContentText("© 2025 SUPSI – Emre Yildirim Niccolò Xhyra");
            a.showAndWait();
        });
    }

    @Override
    public void win() {
        Platform.runLater(() -> {
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("You Win!");
            a.setHeaderText(null);
            a.setContentText("Congratulations, you cleared the minefield!");
            a.showAndWait();
        });
    }

    @Override
    public void lose() {
        Platform.runLater(() -> {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Game Over");
            a.setHeaderText("Boom! You hit a mine.");
            a.setContentText("Try again with File → New.");
            a.showAndWait();
        });
    }

    @Override
    public void move() {
        gameModel.move();
        views.forEach(DataView::update);
    }
}