package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;
import ch.supsi.minesweeper.util.AppPreferences;
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
        Platform.runLater(() -> {

            /* numero mine di default da config.properties */
            int bombsPref = AppPreferences.getBombs();

            int max = gameModel.getRows() * gameModel.getCols() - 1;
            int bombs = Math.max(1, Math.min(bombsPref, max));   // clamp di sicurezza

            gameModel.setMines(bombs);
            gameModel.newGame();

            views.forEach(DataView::update);

            /* messaggio che informa il giocatore (requisito #2) */
            Alert info = new Alert(AlertType.INFORMATION);
            info.setTitle("Nuova partita");
            info.setHeaderText(null);
            info.setContentText("Sono state nascoste " + bombs + " mine.");
            info.showAndWait();
        });
    }

    @Override
    public void save() {
        gameModel.save();
        views.forEach(DataView::update);
    }

    @Override
    public void help() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setHeaderText("How to play");
            alert.setContentText(
                    "• Left-click to reveal a cell\n"
                            + "• Right-click to flag / unflag\n"
                            + "• Reveal all safe cells to win."
            );
            alert.showAndWait();
        });
    }

    @Override
    public void about() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Minesweeper JavaFX");
            alert.setContentText("© 2025 SUPSI – Memet Emre Yildirim, Niccolò Xhyra");
            alert.showAndWait();
        });
    }

    @Override
    public void win() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("You Win!");
            alert.setHeaderText(null);
            alert.setContentText("Congratulations, you cleared the minefield!");
            alert.showAndWait();
        });
    }

    @Override
    public void lose() {
        Platform.runLater(() -> {
            gameModel.reset();                    // 🔸 disattiva la partita
            views.forEach(DataView::update);      // 🔸 aggiorna le view (ora si bloccano)
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Game Over");
            alert.setHeaderText("Boom! You hit a mine.");
            alert.setContentText("Try again with File → New.");
            alert.showAndWait();
        });
    }

    @Override
    public void move() {
        gameModel.move();
        views.forEach(DataView::update);
    }
}