package ch.supsi.minesweeper;

import ch.supsi.minesweeper.controller.GameController;
import ch.supsi.minesweeper.persistence.JsonGameRepository;
import ch.supsi.minesweeper.service.DefaultGameService;
import ch.supsi.minesweeper.service.GameService;
import ch.supsi.minesweeper.service.DefaultPreferenceService;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.uimodel.GameUiModel;
import ch.supsi.minesweeper.view.*;
import ch.supsi.minesweeper.view.UiDialogs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFx extends Application {

    public static final String BUNDLE_BASE = "i18n.messages";

    private final ControlledFxView menuBarView;
    private final ControlledFxView gameBoardView;
    private final UncontrolledFxView feedbackView;

    private final ResourceBundle bundle;

    public MainFx() {


        Locale locale = Locale.forLanguageTag(DefaultPreferenceService.getInstance().getLang());
        bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);

        AbstractModel model = GameModel.getInstance();
        JsonGameRepository repo = new JsonGameRepository();
        GameService gameService = new DefaultGameService((GameModel) model, repo);

        AbstractModel uiModel = new GameUiModel(gameService);

        menuBarView    = MenuBarViewFxml.getInstance(bundle);
        gameBoardView  = GameBoardViewFxml.getInstance(bundle);
        feedbackView   = UserFeedbackViewFxml.getInstance(bundle);

        GameController controller = GameController.getInstance();

        controller.setGameService(gameService);
        controller.attachMenuController(bundle, DefaultPreferenceService.getInstance().getBombs());
        
        menuBarView.initialize( controller, uiModel);
        gameBoardView.initialize( controller, uiModel);
        feedbackView.initialize(uiModel);

        controller.initialize(List.of(menuBarView, gameBoardView, feedbackView));
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();
        root.setTop   (menuBarView.getNode());
        root.setCenter(gameBoardView.getNode());
        root.setBottom(feedbackView.getNode());

        Scene scene = new Scene(root);
        stage.setTitle(bundle.getString("app.title"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(evt -> {
            evt.consume();
            UiDialogs.confirm(
                    bundle.getString("quit.title"),
                    bundle.getString("quit.ask"),
                    () -> Platform.exit()
            );
        });
    }

    public static void main(String[] args) { launch(args); }
}