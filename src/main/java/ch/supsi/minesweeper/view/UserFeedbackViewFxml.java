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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void initialize(AbstractModel model) {
        this.gameModel = (GameModel) model;
    }

    @Override
    public Node getNode() {
        return this.containerPane;
    }

    @Override
    public void update() {
        // get your data from the model, if needed
        // then update this view here
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date(System.currentTimeMillis());
        this.userFeedbackBar.setText(" updated..." + dateFormat.format(date));
    }

}
