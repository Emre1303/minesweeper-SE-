package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class UserFeedbackViewFxml implements UncontrolledFxView {

    private static UserFeedbackViewFxml myself;
    private GameModel gameModel;

    @FXML
    private ScrollPane containerPane;

    @FXML
    private Text userFeedbackBar;

    private UserFeedbackViewFxml() {}

    public static UserFeedbackViewFxml getInstance() {
        if (myself == null) {
            myself = new UserFeedbackViewFxml();
            try {
                URL fxmlUrl = UserFeedbackViewFxml.class.getResource("/userfeedbackbar.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                loader.setController(myself);
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException("Impossibile caricare userfeedbackbar.fxml", e);
            }
        }
        return myself;
    }

    @Override
    public void initialize(AbstractModel model) {
        this.gameModel = (GameModel) model;
        update();
    }

    @Override
    public Node getNode() {
        return containerPane;
    }

    @Override
    public void update() {
        int totalMines = gameModel.getMines();
        int flags      = gameModel.getFlaggedCount();
        int remaining  = totalMines - flags;
        userFeedbackBar.setText(String.format("Bombs: %d/%d", remaining, totalMines));
    }
}