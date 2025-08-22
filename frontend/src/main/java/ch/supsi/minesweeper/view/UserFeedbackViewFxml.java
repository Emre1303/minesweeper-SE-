package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.util.AppPreferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserFeedbackViewFxml implements UncontrolledFxView {

    private static UserFeedbackViewFxml myself;
    private final ResourceBundle bundle;
    private GameModel gameModel;
    @FXML private ScrollPane containerPane;
    @FXML private Text       userFeedbackBar;
    private boolean overrideActive = false;
    private String  overrideMessage = null;
    private final PauseTransition autoClear = new PauseTransition(Duration.seconds(3));

    private UserFeedbackViewFxml(ResourceBundle bundle) {
        this.bundle = bundle;
        autoClear.setOnFinished(ev -> clearMessage());
    }

    public static UserFeedbackViewFxml getInstance(ResourceBundle bundle) {
        if (myself == null) {
            myself = new UserFeedbackViewFxml(bundle);
            try {
                URL url = UserFeedbackViewFxml.class.getResource("/userfeedbackbar.fxml");
                FXMLLoader loader = new FXMLLoader(url, bundle);
                loader.setController(myself);
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException("Impossibile caricare userfeedbackbar.fxml", e);
            }
        }
        return myself;
    }

    public static UserFeedbackViewFxml getInstance() {
        ResourceBundle def = ResourceBundle.getBundle(
                "i18n.messages",
                Locale.forLanguageTag(AppPreferences.getLang()));
        return getInstance(def);
    }

    @Override public void initialize(AbstractModel model) {
        gameModel = (GameModel) model;

        update();
    }
    @Override public Node getNode() { return containerPane; }


    public void showMessage(String message) {
        overrideActive  = true;
        overrideMessage = message;
        userFeedbackBar.setText(message);
        autoClear.playFromStart(); // dopo 3s torna a bombe rimanenti
    }

    public void showMessageSticky(String message) {
        autoClear.stop();
        overrideActive  = true;
        overrideMessage = message;
        userFeedbackBar.setText(message);
    }

    public void clearMessage() {
        overrideActive  = false;
        overrideMessage = null;
        update();
    }

    @Override
    public void update() {
        if (overrideActive && overrideMessage != null) {
            userFeedbackBar.setText(overrideMessage);
            return;
        }
        int total = gameModel.getMines();
        int flags = gameModel.getFlaggedCount();
        int remaining = total - flags;
        String fmt = bundle.getString("status.bombs");
        userFeedbackBar.setText(java.text.MessageFormat.format(fmt, remaining, total));
    }
}
