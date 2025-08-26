package ch.supsi.minesweeper.view;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.Alert;


public final class UiDialogs {
    private UiDialogs() {}



    public static void showPreferencesDialog(ResourceBundle bundle,
                                             int currentBombs,
                                             String currentLang,
                                             int maxBombs,
                                             BiConsumer<Integer, String> onOk,
                                             Consumer<String> onError) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle(bundle.getString("menu.preferences"));
        dlg.setHeaderText(bundle.getString("prefs.header"));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField bombsField = new TextField(String.valueOf(currentBombs));
        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("en", "it");
        langBox.setValue(currentLang);

        grid.addRow(0, new Label(bundle.getString("prefs.bombs.label")), bombsField);
        grid.addRow(1, new Label(bundle.getString("prefs.lang.label")),  langBox);

        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().filter(bt -> bt == ButtonType.OK).ifPresent(bt -> {
            try {
                int bombs = Integer.parseInt(bombsField.getText().trim());
                if (bombs < 1 || bombs > maxBombs) throw new NumberFormatException();

                String lang = langBox.getValue();
                onOk.accept(bombs, lang);
            } catch (NumberFormatException ex) {
                String msg = bundle.getString("prefs.error") + " 1–" + maxBombs + ".";
                onError.accept(msg);
            }
        });
    }

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