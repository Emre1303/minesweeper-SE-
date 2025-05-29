package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.util.AppPreferences;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;

public class MenuBarViewFxml implements ControlledFxView {

    private static MenuBarViewFxml myself;
    private GameEventHandler gameEventHandler;
    private GameModel        gameModel;
    @FXML private MenuBar  menuBar;
    @FXML private MenuItem newMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem quitMenuItem;          // se lo usi altrove
    @FXML private MenuItem preferencesMenuItem;   // <— handler aggiunto
    @FXML private MenuItem helpMenuItem;
    @FXML private MenuItem aboutMenuItem;

    private MenuBarViewFxml() { }

    public static MenuBarViewFxml getInstance() {
        if (myself == null) {
            myself = new MenuBarViewFxml();
            try {
                URL fxmlUrl = MenuBarViewFxml.class.getResource("/menubar.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                loader.setController(myself);
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException("Errore caricamento menubar.fxml", e);
            }
        }
        return myself;
    }

    @Override
    public void initialize(EventHandler eventHandler, AbstractModel model) {
        this.gameEventHandler = (GameEventHandler) eventHandler;
        this.gameModel        = (GameModel) model;
        createBehaviour();
    }

    @Override public Node getNode() { return menuBar; }

    @Override public void update() {
    }
    private void createBehaviour() {

        newMenuItem.setOnAction(e -> gameEventHandler.newGame());
        saveMenuItem.setOnAction(e -> gameEventHandler.save());
        helpMenuItem.setOnAction(e -> gameEventHandler.help());
        aboutMenuItem.setOnAction(e -> gameEventHandler.about());

        /* ---------- Preferences…  ------------------------------------ */
        preferencesMenuItem.setOnAction(e -> showPreferencesDialog());
    }


    private void showPreferencesDialog() {

        int currentBombs = AppPreferences.getBombs();
        String currentLang = AppPreferences.getLang();

        int maxBombs = gameModel.getRows() * gameModel.getCols() - 1;

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Preferences");
        dlg.setHeaderText("Modifica preferenze (si applicano al riavvio)");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);

        TextField bombsField = new TextField(String.valueOf(currentBombs));
        bombsField.setPrefColumnCount(5);

        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("en", "it");
        langBox.setValue(currentLang);

        grid.addRow(0, new Label("Numero mine di default:"), bombsField);
        grid.addRow(1, new Label("Lingua:"), langBox);

        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait()
                .filter(bt -> bt == ButtonType.OK)
                .ifPresent(bt -> {
                    try {
                        int bombs = Integer.parseInt(bombsField.getText().trim());
                        if (bombs < 1 || bombs > maxBombs) {
                            throw new NumberFormatException();
                        }

                        AppPreferences.setBombs(bombs);
                        AppPreferences.setLang(langBox.getValue());
                        AppPreferences.save();

                        new Alert(Alert.AlertType.INFORMATION,
                                "Preferenze salvate.\n" +
                                        "Riavvia l’applicazione e avvia una nuova partita\n" +
                                        "per applicare i cambiamenti.")
                                .showAndWait();

                    } catch (NumberFormatException ex) {
                        new Alert(Alert.AlertType.ERROR,
                                "Il numero di mine deve essere fra 1 e " + maxBombs + ".")
                                .showAndWait();
                    }
                });
    }
}