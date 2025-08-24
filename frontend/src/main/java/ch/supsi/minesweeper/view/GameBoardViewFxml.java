package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.controller.PlayerEventHandler;
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
import java.util.*;

public class GameBoardViewFxml implements ControlledFxView {

    private static GameBoardViewFxml myself;
    private static final double BUTTON_SIZE = 37;
    private static final double IMAGE_SIZE  = 30;

    private PlayerEventHandler playerEventHandler;
    private GameModel          gameModel;

    @FXML private GridPane containerPane;
    private final Map<Integer, Image> numberImages = new HashMap<>();
    private Image flagImg;
    private Image bombImg;

    private GameBoardViewFxml() {
        loadImages();
    }

    public static GameBoardViewFxml getInstance(ResourceBundle bundle) {
        if (myself == null) {
            myself = new GameBoardViewFxml();
            try {
                URL url = GameBoardViewFxml.class.getResource("/gameboard.fxml");
                FXMLLoader loader = new FXMLLoader(url, bundle);
                loader.setController(myself);
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException("Errore caricamento gameboard.fxml", e);
            }
        }
        return myself;
    }

    @Override
    public void initialize(EventHandler evt, AbstractModel model) {
        playerEventHandler = evt;
        gameModel          = (GameModel) model;
        setupGrid();
    }

    @Override
    public Node getNode() {
        return containerPane;
    }

    @Override
    public void update() {
        for (Node n : containerPane.getChildren()) {
            Button btn = (Button) n;
            int row = Optional.ofNullable(GridPane.getRowIndex(btn)).orElse(0);
            int col = Optional.ofNullable(GridPane.getColumnIndex(btn)).orElse(0);

            if (gameModel.isRevealed(row, col)) {
                if (gameModel.hasMineAt(row, col)) {
                    btn.setGraphic(makeIcon(bombImg));
                } else {
                    int cnt = gameModel.getNeighborCountAt(row, col);
                    if (cnt > 0) {
                        btn.setGraphic(makeIcon(numberImages.get(cnt)));
                    } else {
                        btn.setGraphic(null);
                    }
                }
                btn.setDisable(true);
            }
            else if (gameModel.isFlagged(row, col)) {
                btn.setGraphic(makeIcon(flagImg));
                btn.setDisable(false);
            }
            else {
                btn.setGraphic(null);
                btn.setDisable(!gameModel.isStarted());
            }
        }
    }

    private void setupGrid() {
        for (Node n : containerPane.getChildren()) {
            Button btn = (Button) n;
            btn.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setDisable(true);

            int row = Optional.ofNullable(GridPane.getRowIndex(btn)).orElse(0);
            int col = Optional.ofNullable(GridPane.getColumnIndex(btn)).orElse(0);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> handleClick(e, row, col));
        }
    }

    private void handleClick(MouseEvent e, int r, int c) {
        if (!gameModel.isStarted()) return;

        if (e.getButton() == MouseButton.SECONDARY) {
            playerEventHandler.toggleFlag(r, c);
        } else if (e.getButton() == MouseButton.PRIMARY && !gameModel.isFlagged(r, c)) {
            playerEventHandler.reveal(r, c);
        }
        e.consume();
    }


    private void loadImages() {
        for (int i = 1; i <= 8; i++) {
            numberImages.put(i, new Image(getClass().getResourceAsStream("/images/" + i + ".png")));
        }
        flagImg = new Image(getClass().getResourceAsStream("/images/flag.png"));
        bombImg = new Image(getClass().getResourceAsStream("/images/bomb.png"));
    }


    private ImageView makeIcon(Image img) {
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);
        iv.setFitWidth(IMAGE_SIZE);
        iv.setFitHeight(IMAGE_SIZE);
        return iv;
    }
}