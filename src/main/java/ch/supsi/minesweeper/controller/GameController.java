package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.view.DataView;
import ch.supsi.minesweeper.util.AppPreferences;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GameController implements GameEventHandler, PlayerEventHandler {

    private static GameController myself;
    private final GameModel gameModel;
    private List<DataView> views;
    private GameController() { gameModel = GameModel.getInstance(); }
    public static GameController getInstance() {
        if (myself == null) myself = new GameController();
        return myself;
    }

    public void initialize(List<DataView> views) { this.views = views; }

    private ResourceBundle rb() {
        return ResourceBundle.getBundle(
                "i18n.messages",
                Locale.forLanguageTag(AppPreferences.getLang()));
    }

    @Override
    public void newGame() {
        Platform.runLater(() -> {
            int pref = AppPreferences.getBombs();
            int max  = gameModel.getRows()*gameModel.getCols() - 1;
            int bombs = Math.max(1, Math.min(pref, max));

            gameModel.setMines(bombs);
            gameModel.newGame();
            views.forEach(DataView::update);

            ResourceBundle rb = rb();
            Alert info = new Alert(AlertType.INFORMATION);
            info.setTitle(rb.getString("dialog.new.title"));
            info.setHeaderText(null);
            info.setContentText(
                    MessageFormat.format(rb.getString("dialog.new.body"), bombs));
            info.showAndWait();
        });
    }

    @Override public void save() { gameModel.save(); views.forEach(DataView::update); }

    @Override
    public void help() {
        Platform.runLater(() -> {
            ResourceBundle rb = rb();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle(rb.getString("help.title"));
            a.setHeaderText(rb.getString("help.header"));
            a.setContentText(rb.getString("help.content"));
            a.showAndWait();
        });
    }
    @Override
    public void about() {
        Platform.runLater(() -> {
            ResourceBundle rb = rb();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle(rb.getString("about.title"));
            a.setHeaderText(rb.getString("about.header"));
            a.setContentText(rb.getString("about.content"));
            a.showAndWait();
        });
    }

    @Override
    public void win() {
        Platform.runLater(() -> {
            ResourceBundle rb = rb();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle(rb.getString("alert.win.title"));
            a.setHeaderText(null);
            a.setContentText(rb.getString("alert.win.text"));
            a.showAndWait();
        });
    }

    @Override
    public void lose() {
        Platform.runLater(() -> {
            ResourceBundle rb = ResourceBundle.getBundle(
                    "i18n.messages",
                    Locale.forLanguageTag(AppPreferences.getLang()));

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(rb.getString("alert.lose.title"));
            alert.setHeaderText(rb.getString("alert.lose.header"));
            alert.setContentText(rb.getString("alert.lose.text"));
            alert.showAndWait();
        });
    }

    @Override
    public void move() { gameModel.move(); views.forEach(DataView::update); }
}