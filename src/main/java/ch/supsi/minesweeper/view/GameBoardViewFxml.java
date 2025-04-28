package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GameBoardViewFxml implements ControlledFxView {

    private static GameBoardViewFxml myself;

    private PlayerEventHandler playerEventHandler;
    private GameEventHandler   gameEventHandler;
    private GameModel          gameModel;

    @FXML private GridPane containerPane;

    @FXML private Button cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07, cell08;
    @FXML private Button cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17, cell18;
    @FXML private Button cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27, cell28;
    @FXML private Button cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37, cell38;
    @FXML private Button cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47, cell48;
    @FXML private Button cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57, cell58;
    @FXML private Button cell60, cell61, cell62, cell63, cell64, cell65, cell66, cell67, cell68;
    @FXML private Button cell70, cell71, cell72, cell73, cell74, cell75, cell76, cell77, cell78;
    @FXML private Button cell80, cell81, cell82, cell83, cell84, cell85, cell86, cell87, cell88;

    private final Map<Integer, Image> numberImages = new HashMap<>();
    private Image flagImage;
    private Image bombImage;

    private GameBoardViewFxml() {
        loadImages();
    }

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

    private void loadImages() {
        for (int i = 1; i <= 8; i++) {
            numberImages.put(i, new Image(getClass().getResourceAsStream("/images/" + i + ".png")));
        }
        flagImage = new Image(getClass().getResourceAsStream("/images/flag.png"));
        bombImage = new Image(getClass().getResourceAsStream("/images/bomb.png"));
    }

    @Override
    public void initialize(EventHandler eventHandler, AbstractModel model) {
        this.playerEventHandler = (PlayerEventHandler) eventHandler;
        this.gameEventHandler   = (GameEventHandler)   eventHandler;
        this.gameModel          = (GameModel)           model;
        setupGrid();
        update(); // pulisce la griglia
    }

    private void setupGrid() {
        for (Node node : containerPane.getChildren()) {
            if (node instanceof Button btn) {
                int row = GridPane.getRowIndex(btn) == null ? 0 : GridPane.getRowIndex(btn);
                int col = GridPane.getColumnIndex(btn) == null ? 0 : GridPane.getColumnIndex(btn);
                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> handleClick(evt, row, col, btn));
            }
        }
    }

    private void handleClick(MouseEvent evt, int row, int col, Button btn) {
        if (evt.getButton() == MouseButton.SECONDARY) {
            gameModel.toggleFlag(row, col);
            if (gameModel.isFlagged(row, col)) {
                setButtonGraphic(btn, flagImage);
            } else {
                btn.setGraphic(null);
            }
            evt.consume();
        } else if (evt.getButton() == MouseButton.PRIMARY) {
            if (!gameModel.isFlagged(row, col)) {
                revealCell(row, col, btn);
            }
            evt.consume();
        }
    }

    private void revealCell(int row, int col, Button btn) {
        int result = gameModel.revealCell(row, col);
        btn.setDisable(true);
        if (result < 0) {
            setButtonGraphic(btn, bombImage);
            disableAll();
            gameEventHandler.lose();
        } else {
            if (result > 0) {
                setButtonGraphic(btn, numberImages.get(result));
            }
            if (gameModel.isWin()) {
                disableAll();
                gameEventHandler.win();
            }
        }
    }

    private void disableAll() {
        for (Node node : containerPane.getChildren()) {
            if (node instanceof Button other) {
                int r = GridPane.getRowIndex(other) == null ? 0 : GridPane.getRowIndex(other);
                int c = GridPane.getColumnIndex(other) == null ? 0 : GridPane.getColumnIndex(other);
                if (gameModel.hasMineAt(r, c)) {
                    setButtonGraphic(other, bombImage);
                }
                other.setDisable(true);
            }
        }
    }

    private void setButtonGraphic(Button btn, Image img) {
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);
        iv.setFitWidth(btn.getPrefWidth());
        iv.setFitHeight(btn.getPrefHeight());
        btn.setGraphic(iv);
    }

    @Override
    public Node getNode() {
        return containerPane;
    }

    @Override
    public void update() {
        for (Node node : containerPane.getChildren()) {
            if (node instanceof Button btn) {
                btn.setGraphic(null);
                btn.setDisable(false);
            }
        }
    }
}