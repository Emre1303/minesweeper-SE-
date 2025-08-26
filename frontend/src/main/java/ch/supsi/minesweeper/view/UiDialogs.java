package ch.supsi.minesweeper.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;


public final class UiDialogs {
    private UiDialogs() {}





    public static void about(String title, String appName, String version, String description, String author, String builtOn) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(title);
            a.setHeaderText(appName + "  |  Version: " + version);
            a.setContentText(
                    description + "\n"
                            + "Author: " + author + "\n"
                            + "Built on: " + builtOn
            );
            a.showAndWait();
        });
    }


}