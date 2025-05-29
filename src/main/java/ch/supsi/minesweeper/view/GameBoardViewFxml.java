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
    private Image flagImage, bombImage;

    private GameBoardViewFxml(ResourceBundle bundle) {
        this.bundle = bundle;
        loadImages();
    }

    public static GameBoardViewFxml getInstance(ResourceBundle bundle) {
        if (myself == null) {
            myself = new GameBoardViewFxml(bundle);
            try {
                URL url = GameBoardViewFxml.class.getResource("/gameboard.fxml");
                FXMLLoader loader = new FXMLLoader(url, bundle);      // 🔸
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
        update();
    }
    @Override public Node getNode() { return containerPane; }

    // setup griglia & click
    private void setupGrid() {
        for (Node n : containerPane.getChildren()) if (n instanceof Button btn) {
            btn.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
            btn.setDisable(true);                           // bloccato all’avvio

            int row = Optional.ofNullable(GridPane.getRowIndex(btn)).orElse(0);
            int col = Optional.ofNullable(GridPane.getColumnIndex(btn)).orElse(0);
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> handleClick(e, row, col, btn));
        }
    }

    private void handleClick(MouseEvent e, int r, int c, Button btn) {
        if (!gameModel.isStarted()) return;                 // partita non attiva

        if (e.getButton() == MouseButton.SECONDARY) {
            gameModel.toggleFlag(r, c);
            btn.setGraphic(gameModel.isFlagged(r, c) ? flagIcon() : null);
            UserFeedbackViewFxml.getInstance().update();
        } else if (e.getButton() == MouseButton.PRIMARY && !gameModel.isFlagged(r,c)) {
            revealCell(r, c, btn);
        }
        e.consume();
    }

    private void revealCell(int r, int c, Button btn) {
        int res = gameModel.revealCell(r, c);
        if (res < 0) {
            btn.setGraphic(bombIcon());
            disableAll();
            gameEventHandler.lose();
        } else {
            btn.setDisable(true);
            if (res > 0) {
                btn.setGraphic(makeIcon(numberImages.get(res)));
            }
            if (gameModel.isWin()) { disableAll(); gameEventHandler.win(); }
        }
    }
    private void disableAll() {
        for (Node n : containerPane.getChildren()) if (n instanceof Button b) {
            int rr = Optional.ofNullable(GridPane.getRowIndex(b)).orElse(0);
            int cc = Optional.ofNullable(GridPane.getColumnIndex(b)).orElse(0);
            if (gameModel.hasMineAt(rr, cc)) b.setGraphic(bombIcon());
            b.setDisable(true);
        }
    }

    private void loadImages() {
        for (int i = 1; i <= 8; i++)
            numberImages.put(i, new Image(getClass().getResourceAsStream("/images/"+i+".png")));
        flagImage = new Image(getClass().getResourceAsStream("/images/flag.png"));
        bombImage = new Image(getClass().getResourceAsStream("/images/bomb.png"));
    }
    private ImageView flagIcon() { return makeIcon(flagImage); }
    private ImageView bombIcon() { return makeIcon(bombImage); }
    private ImageView makeIcon(Image img) {
        ImageView iv = new ImageView(img); iv.setPreserveRatio(true);
        iv.setFitWidth(IMAGE_SIZE); iv.setFitHeight(IMAGE_SIZE); return iv;
    }

    @Override
    public void update() {
        boolean active = gameModel.isStarted();

        for (Node n : containerPane.getChildren()) {
            if (n instanceof Button b) {
                b.setGraphic(null);
                b.setDisable(!active);
            }
        }
    }
}