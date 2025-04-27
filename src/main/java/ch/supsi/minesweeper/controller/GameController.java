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

    /** Registra le view all’avvio */
    public void initialize(List<DataView> views) {
        this.views = views;
    }

    @Override
    public void newGame() {
        // ri‐inizializza il modello
        this.gameModel.newGame();
        // aggiorna le view
        this.views.forEach(DataView::update);
    }

    @Override
    public void save() {
        // salva lo stato dal modello
        this.gameModel.save();
        // aggiorna le view
        this.views.forEach(DataView::update);
    }

    @Override
    public void help() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setHeaderText("How to play");
            alert.setContentText(
                    """
                Objective
                ─────────
                Clear all safe cells without triggering a mine.

                Controls
                • Left-click  → reveal a cell
                • Right-click → place/remove a flag on the cell
                • A revealed number tells how many mines are in the 8 adjacent cells.

                Rules
                • First click may reveal a mine – there is no guaranteed safe start.
                • You can place at most as many flags as the number of hidden mines.
                • Win by revealing every non-mine cell; clicking on a mine ends the game.

                Menu shortcuts
                • File ▸ New…    Ctrl/Cmd N
                • File ▸ Open…   Ctrl/Cmd O
                • File ▸ Save    Ctrl/Cmd S
                • File ▸ Quit    Ctrl/Cmd Q
                """);
            alert.showAndWait();
        });
    }

    @Override
    public void about() {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Minesweeper JavaFX");
            alert.setContentText(
                    """
                    Classic Minesweeper clone written in Java 17 / JavaFX 17.

                    © 2025 Memet Emre Yildirim, Niccolò Xhyra – SUPSI
                    Licensed under the MIT License.
                    """ );
            alert.showAndWait();
        });
    }

    @Override
    public void move() {
        this.gameModel.move();
        this.views.forEach(DataView::update);
    }

}