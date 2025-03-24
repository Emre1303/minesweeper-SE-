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

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem preferencesMenuItem;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private MenuItem helpMenuItem;

    private MenuBarViewFxml() {}

    public static MenuBarViewFxml getInstance() {
        if (myself == null) {
            myself = new MenuBarViewFxml();

            try {
                URL fxmlUrl = MenuBarViewFxml.class.getResource("/menubar.fxml");
                if (fxmlUrl != null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
                    fxmlLoader.setController(myself);
                    fxmlLoader.load();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return myself;
    }

    @Override
    public void initialize(EventHandler eventHandler, AbstractModel model) {
        this.createBehaviour();
        this.gameEventHandler = (GameEventHandler) eventHandler;
        this.gameModel = (GameModel) model;
    }

    private void createBehaviour() {
        // new
        this.newMenuItem.setOnAction(event -> this.gameEventHandler.newGame());

        // save
        this.saveMenuItem.setOnAction(event -> this.gameEventHandler.save());

        // add event handlers for all necessary menu items
        // ...
    }

    @Override
    public Node getNode() {
        return this.menuBar;
    }

    @Override
    public void update() {
        // get your data from the model, if needed
        // then update this view here
        System.out.println(this.getClass().getSimpleName() + " updated..." + System.currentTimeMillis());
    }

}
