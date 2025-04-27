package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /** Registra le view all’avvio */
    public void initialize(List<DataView> views) {
        this.views = views;
    }

    /** Nuova partita con scelta dinamica del numero di mine */
    @Override
    public void newGame() {
        Platform.runLater(() -> {

            List<Integer> options = IntStream
                    .rangeClosed(1, gameModel.getRows() * gameModel.getCols() - 1)
                    .boxed()
                    .collect(Collectors.toList());

            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(
                    gameModel.getMines(), options);
            dialog.setTitle("Nuova Partita");
            dialog.setHeaderText("Imposta numero di mine");
            dialog.setContentText(
                    String.format("Seleziona quante mine (1–%d):", options.get(options.size() - 1))
            );

            Optional<Integer> result = dialog.showAndWait();
            result.ifPresent(count -> {
                gameModel.setMines(count);
                gameModel.newGame();
                views.forEach(DataView::update);
            });
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
                    "• Left-click to reveal a cell\n" +
                            "• Right-click to flag/unflag\n" +
                            "• Reveal all safe cells to win."
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
