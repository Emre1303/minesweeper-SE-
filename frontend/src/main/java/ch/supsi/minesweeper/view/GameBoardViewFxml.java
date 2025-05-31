package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.PlayerEventHandler;
import ch.supsi.minesweeper.util.AppPreferences;
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
    private final ResourceBundle bundle;
    private static final double BUTTON_SIZE = 37;
    private static final double IMAGE_SIZE  = 30;
    private PlayerEventHandler playerEventHandler;
    private GameEventHandler   gameEventHandler;
    private GameModel          gameModel;
    @FXML private GridPane containerPane;
    private final Map<Integer, Image> numberImages = new HashMap<>();
    private Image flagImg, bombImg;

    private GameBoardViewFxml(ResourceBundle bundle) {
        this.bundle = bundle;
        loadImages();
    }

    public static GameBoardViewFxml getInstance(ResourceBundle bundle) {
        if (myself == null) {
            myself = new GameBoardViewFxml(bundle);
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
    public static GameBoardViewFxml getInstance() {
        ResourceBundle def = ResourceBundle.getBundle(
                "i18n.messages",
                Locale.forLanguageTag(AppPreferences.getLang()));
        return getInstance(def);
    }

    @Override
    public void initialize(EventHandler evt, AbstractModel model) {
        playerEventHandler = (PlayerEventHandler) evt;
        gameEventHandler   = (GameEventHandler)   evt;
        gameModel          = (GameModel) model;
        setupGrid();
    }
    @Override public Node getNode() { return containerPane; }

    @Override public void update() {
        for (Node n : containerPane.getChildren())
            if (n instanceof Button b) { b.setGraphic(null); b.setDisable(false); }
    }

    private void setupGrid() {
        for (Node n : containerPane.getChildren()) if (n instanceof Button btn) {
            btn.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setDisable(true);

            int row = Optional.ofNullable(GridPane.getRowIndex(btn)).orElse(0);
            int col = Optional.ofNullable(GridPane.getColumnIndex(btn)).orElse(0);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> handleClick(e, row, col, btn));
        }
    }

    private void handleClick(MouseEvent e, int r, int c, Button btn) {
        if (!gameModel.isStarted()) return;

        if (e.getButton() == MouseButton.SECONDARY) {
            gameModel.toggleFlag(r, c);
            btn.setGraphic(gameModel.isFlagged(r,c) ? makeIcon(flagImg) : null);
            UserFeedbackViewFxml.getInstance().update();
        } else if (e.getButton() == MouseButton.PRIMARY && !gameModel.isFlagged(r,c)) {
            revealCell(r, c, btn);
        }
        e.consume();
    }

    private void revealCell(int r, int c, Button ignored) {
        List<int[]> opened = gameModel.revealArea(r, c);

        for (int[] pos : opened) {
            int row = pos[0], col = pos[1];
            Button b = getButtonAt(row, col);

            if (gameModel.hasMineAt(row, col)) {
                b.setGraphic(makeIcon(bombImg));
            } else {
                int cnt = gameModel.getNeighborCountAt(row, col);
                if (cnt > 0) b.setGraphic(makeIcon(numberImages.get(cnt)));
            }
            b.setDisable(true);
        }

        if (gameModel.hasMineAt(r, c)) {
            disableAll();
            gameEventHandler.lose();
        } else if (gameModel.isWin()) {
            disableAll();
            gameEventHandler.win();
        }
    }
    private void disableAll() {
        for (Node n : containerPane.getChildren()) if (n instanceof Button b) {
            int rr = Optional.ofNullable(GridPane.getRowIndex(b)).orElse(0);
            int cc = Optional.ofNullable(GridPane.getColumnIndex(b)).orElse(0);
            if (gameModel.hasMineAt(rr, cc)) b.setGraphic(makeIcon(bombImg));
            b.setDisable(true);
        }
    }

    private void loadImages() {
        for (int i = 1; i <= 8; i++)
            numberImages.put(i,
                    new Image(getClass().getResourceAsStream("/images/" + i + ".png")));
        flagImg = new Image(getClass().getResourceAsStream("/images/flag.png"));
        bombImg = new Image(getClass().getResourceAsStream("/images/bomb.png"));
    }
    private Button getButtonAt(int row, int col) {
        for (Node n : containerPane.getChildren()) {
            if (n instanceof Button b &&
                    Objects.equals(GridPane.getRowIndex(b), row) &&
                    Objects.equals(GridPane.getColumnIndex(b), col)) {
                return b;
            }
        }
        throw new IllegalStateException("Button non trovato (" + row + "," + col + ")");
    }

    private ImageView makeIcon(Image img) {
        ImageView iv = new ImageView(img);
        iv.setPreserveRatio(true);
        iv.setFitWidth(IMAGE_SIZE);
        iv.setFitHeight(IMAGE_SIZE);
        return iv;
    }
}