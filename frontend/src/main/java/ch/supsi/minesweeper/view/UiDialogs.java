package ch.supsi.minesweeper.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public final class UiDialogs {
    private UiDialogs() {}



    /*public static void error(String title, String content) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(content);
            a.showAndWait();
        });
    }*/ //Non usato

    public static void about(String appName, String version, String description, String author, String builtOn) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("About " + appName);
            a.setHeaderText(appName + "  |  Versione: " + version);
            a.setContentText(
                    description + "\n"
                            + "Author: " + author + "\n"
                            + "Built on: " + builtOn
            );
            a.showAndWait();
        });
    }

    public static void confirm(String title, String content, Runnable onYes) {
        Platform.runLater(() -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.setTitle(title);
            Optional<ButtonType> res = confirm.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.YES) {
                onYes.run();
            }
        });
    }
}