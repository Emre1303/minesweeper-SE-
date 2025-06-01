package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.model.AbstractModel;
import ch.supsi.minesweeper.model.GameEventHandler;
import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.util.AppPreferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuBarViewFxml implements ControlledFxView {

    private final ResourceBundle bundle;
    private static MenuBarViewFxml myself;
    private GameEventHandler gameEventHandler;
    private GameModel        gameModel;
    @FXML private MenuBar  menuBar;
    @FXML private MenuItem newMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem quitMenuItem;
    @FXML private MenuItem preferencesMenuItem;
    @FXML private MenuItem helpMenuItem;
    @FXML private MenuItem aboutMenuItem;

    private MenuBarViewFxml(ResourceBundle bundle) { this.bundle = bundle; }

    public static MenuBarViewFxml getInstance(ResourceBundle bundle) {
        if (myself == null) {
            myself = new MenuBarViewFxml(bundle);
            loadFxml(bundle);
        }
        return myself;
    }


    public static MenuBarViewFxml getInstance() {
        ResourceBundle def = ResourceBundle.getBundle(
                "i18n.messages",
                Locale.forLanguageTag(AppPreferences.getLang()));
        return getInstance(def);
    }


    private static void loadFxml(ResourceBundle bundle) {
        try {
            URL url = MenuBarViewFxml.class.getResource("/menubar.fxml");
            FXMLLoader loader = new FXMLLoader(url, bundle);
            loader.setController(myself);
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException("Errore caricamento menubar.fxml", ex);
        }
    }

    @Override
    public void initialize(EventHandler h, AbstractModel m) {
        gameEventHandler = (GameEventHandler) h;
        gameModel        = (GameModel) m;
        createBehaviour();
    }
    @Override public Node getNode() { return menuBar; }
    @Override public void update()  {}


    private void createBehaviour() {

        newMenuItem.setOnAction(e -> gameEventHandler.newGame());
        saveMenuItem.setOnAction(e -> gameEventHandler.save());
        helpMenuItem.setOnAction(e -> gameEventHandler.help());
        aboutMenuItem.setOnAction(e -> gameEventHandler.about());
        preferencesMenuItem.setOnAction(e -> showPreferencesDialog());

        quitMenuItem.setOnAction(e -> {  //da rivedere che non funziona
            ResourceBundle rb = bundle;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    rb.getString("quit.ask"));
            confirm.setHeaderText(null);
            confirm.setTitle(rb.getString("quit.title"));
            confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

            confirm.showAndWait().filter(bt -> bt == ButtonType.YES)
                    .ifPresent(bt -> Platform.exit());
        });
    }
    private void showPreferencesDialog() { //da separare (separation of concern)

        int    currentBombs = AppPreferences.getBombs();
        String currentLang  = AppPreferences.getLang();
        int maxBombs = gameModel.getRows()*gameModel.getCols() - 1;

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle(bundle.getString("menu.preferences"));
        dlg.setHeaderText(bundle.getString("prefs.header"));

        GridPane grid = new GridPane(); grid.setHgap(10); grid.setVgap(10);

        TextField bombsField = new TextField(String.valueOf(currentBombs));
        ComboBox<String> langBox = new ComboBox<>();
        langBox.getItems().addAll("en", "it");
        langBox.setValue(currentLang);

        grid.addRow(0,
                new Label(bundle.getString("prefs.bombs.label")), bombsField);
        grid.addRow(1,
                new Label(bundle.getString("prefs.lang.label")),  langBox);

        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().filter(bt -> bt == ButtonType.OK).ifPresent(bt -> {
            try {
                int bombs = Integer.parseInt(bombsField.getText().trim());
                if (bombs < 1 || bombs > maxBombs) throw new NumberFormatException();

                AppPreferences.setBombs(bombs);
                AppPreferences.setLang(langBox.getValue());
                AppPreferences.save();

                new Alert(Alert.AlertType.INFORMATION,
                        bundle.getString("prefs.saved"))
                        .showAndWait();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR,
                        bundle.getString("prefs.error") + " 1–" + maxBombs + ".")
                        .showAndWait();
            }
        });
    }
}