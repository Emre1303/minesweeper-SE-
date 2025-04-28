package ch.supsi.minesweeper;

import ch.supsi.minesweeper.controller.GameController;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.view.ControlledFxView;
import ch.supsi.minesweeper.view.GameBoardViewFxml;
import ch.supsi.minesweeper.view.MenuBarViewFxml;
import ch.supsi.minesweeper.view.UncontrolledFxView;
import ch.supsi.minesweeper.view.UserFeedbackViewFxml;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class MainFx extends Application {

    public static final String APP_TITLE = "mine sweeper";

    private final AbstractModel          model;
    private final ControlledFxView       menuBarView;
    private final ControlledFxView       gameBoardView;
    private final UncontrolledFxView     feedbackView;

    public MainFx() {
        this.model = GameModel.getInstance();

        // viste
        this.menuBarView     = MenuBarViewFxml.getInstance();
        this.gameBoardView   = GameBoardViewFxml.getInstance();
        this.feedbackView    = UserFeedbackViewFxml.getInstance();

        // inizializzazione MVC
        // il controller gestisce eventi di gioco e movimento di player
        GameController controller = GameController.getInstance();

        menuBarView.initialize(controller, model);
        gameBoardView.initialize(controller, model);
        feedbackView.initialize(model);

        // registra tutte le view per aggiornamenti
        controller.initialize(List.of(
                menuBarView,
                gameBoardView,
                // anche la feedback view implementa DataView e riceverà update()
                feedbackView
        ));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(ev -> {
            ev.consume();
            primaryStage.close();
        });

        BorderPane root = new BorderPane();
        root.setTop   (menuBarView.getNode());
        root.setCenter(gameBoardView.getNode());
        root.setBottom(feedbackView.getNode());

        Scene scene = new Scene(root);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
