package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.net.URL;

public class MenuBarViewFxml implements ControlledFxView {

    private static MenuBarViewFxml myself;

    private GameEventHandler gameEventHandler;
    private GameModel gameModel;

    @FXML private MenuBar menuBar;
    @FXML private Menu fileMenu;
    @FXML private Menu editMenu;
    @FXML private Menu helpMenu;
    @FXML private MenuItem newMenuItem;
    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem saveAsMenuItem;
    @FXML private MenuItem quitMenuItem;
    @FXML private MenuItem preferencesMenuItem;
    @FXML private MenuItem aboutMenuItem;
    @FXML private MenuItem helpMenuItem;

    private MenuBarViewFxml() {}

    public static MenuBarViewFxml getInstance() {
        if (myself == null) {
            myself = new MenuBarViewFxml();
            try {
                URL fxmlUrl = MenuBarViewFxml.class.getResource("/menubar.fxml");
                if (fxmlUrl != null) {
                    FXMLLoader loader = new FXMLLoader(fxmlUrl);
                    loader.setController(myself);
                    loader.load();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return myself;
    }

    @Override
    public void initialize(EventHandler eventHandler, AbstractModel model) {
        this.gameEventHandler = (GameEventHandler) eventHandler;
        this.gameModel        = (GameModel) model;
        this.createBehaviour();
    }

    private void createBehaviour() {
        // New game
        this.newMenuItem.setOnAction(e -> this.gameEventHandler.newGame());

        // Save
        this.saveMenuItem.setOnAction(e -> this.gameEventHandler.save());

        // Help
        this.helpMenuItem.setOnAction(e -> this.gameEventHandler.help());

        // About
        this.aboutMenuItem.setOnAction(e -> this.gameEventHandler.about());

        // (eventuali altri menu… es. open, saveAs, quit, preferences)
    }

    @Override
    public Node getNode() {
        return this.menuBar;
    }

    @Override
    public void update() {
        // aggiorna la view se serve
        System.out.println(getClass().getSimpleName() + " updated at " + System.currentTimeMillis());
    }
}