package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public class GameBoardViewFxml implements ControlledFxView {

    private static GameBoardViewFxml myself;

    private PlayerEventHandler playerEventHandler;
    private GameModel gameModel;

    @FXML
    private GridPane containerPane;

    @FXML private Button cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07, cell08;
    @FXML private Button cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17, cell18;
    @FXML private Button cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27, cell28;
    @FXML private Button cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37, cell38;
    @FXML private Button cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47, cell48;
    @FXML private Button cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57, cell58;
    @FXML private Button cell60, cell61, cell62, cell63, cell64, cell65, cell66, cell67, cell68;
    @FXML private Button cell70, cell71, cell72, cell73, cell74, cell75, cell76, cell77, cell78;
    @FXML private Button cell80, cell81, cell82, cell83, cell84, cell85, cell86, cell87, cell88;

    private GameBoardViewFxml() {}

    public static GameBoardViewFxml getInstance() {
        if (myself == null) {
            myself = new GameBoardViewFxml();
            try {
                URL fxmlUrl = GameBoardViewFxml.class.getResource("/gameboard.fxml");
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
        this.playerEventHandler = (PlayerEventHandler) eventHandler;
        this.gameModel          = (GameModel) model;
        setupGrid();
    }

    /** Imposta i listener di click per ogni cella usando le coordinate dal GridPane */
    private void setupGrid() {
        for (Node node : containerPane.getChildren()) {
            if (node instanceof Button btn) {
                Integer r = GridPane.getRowIndex(btn);
                Integer c = GridPane.getColumnIndex(btn);
                int row = (r == null ? 0 : r);
                int col = (c == null ? 0 : c);

                
            }
        }
    }




    @Override
    public Node getNode() {
        return containerPane;
    }

    @Override
    public void update() {
        // Al newGame(): resetta tutte le celle a coperte
        for (Node node : containerPane.getChildren()) {
            if (node instanceof Button btn) {
                btn.setText("");
                btn.setDisable(false);
            }
        }
    }
}