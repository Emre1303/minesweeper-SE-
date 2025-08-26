package ch.supsi.minesweeper.view;

import ch.supsi.minesweeper.controller.GameController;
import ch.supsi.minesweeper.controller.EventHandler;
import ch.supsi.minesweeper.controller.GameEventHandler;
import ch.supsi.minesweeper.uimodel.GameUiModel;
import ch.supsi.minesweeper.service.DefaultPreferenceService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuBarViewFxml implements ControlledFxView {

    @FXML private MenuBar  menuBar;
    @FXML private MenuItem newMenuItem;
    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem saveAsMenuItem;
    @FXML private MenuItem quitMenuItem;
    @FXML private MenuItem preferencesMenuItem;
    @FXML private MenuItem helpMenuItem;
    @FXML private MenuItem aboutMenuItem;

    private final ResourceBundle   bundle;
    private static MenuBarViewFxml myself;
    private GameEventHandler       gameEventHandler;
    private GameUiModel            game;
    private MenuBarViewFxml(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public static MenuBarViewFxml getInstance(ResourceBundle bundle) {
        if (myself == null) {
            myself = new MenuBarViewFxml(bundle);
            try {
                URL url = MenuBarViewFxml.class.getResource("/menubar.fxml");
                FXMLLoader loader = new FXMLLoader(url, bundle);
                loader.setController(myself);
                loader.load();
            } catch (IOException ex) {
                throw new RuntimeException("Error loading menubar.fxml", ex);
            }
        }
        return myself;
    }

    public static MenuBarViewFxml getInstance() {
        ResourceBundle def = ResourceBundle.getBundle(
                "i18n.messages",
                Locale.forLanguageTag(DefaultPreferenceService.getInstance().getLang()));
        return getInstance(def);
    }

    @Override
    public void initialize(EventHandler h, ch.supsi.minesweeper.model.AbstractModel m) {
        this.gameEventHandler = (GameEventHandler) h;
        this.game        = (GameUiModel) m;
        createBehaviour();
    }

    @Override
    public Node getNode() {
        return menuBar;
    }

    @Override
    public void update() {
        // Non usato nel menu, ma richiesto dall’interfaccia
    }

    private void createBehaviour() {
        // Nuova partita
        newMenuItem.setOnAction(e -> {
            gameEventHandler.newGame();
            enableSaveOptions();
        });

        // Apri partita
        openMenuItem.setOnAction(e -> {
            ((GameController) gameEventHandler).open();
            enableSaveOptions();
        });

        // Salva partita
        saveMenuItem.setOnAction(e -> ((GameController) gameEventHandler).save());

        // Salva come
        saveAsMenuItem.setOnAction(e -> ((GameController) gameEventHandler).saveAs());

        // Help e About
        helpMenuItem.setOnAction(e -> {
            gameEventHandler.userDidSomething();
            gameEventHandler.help();
        });
        aboutMenuItem.setOnAction(e -> {
            gameEventHandler.userDidSomething();
            gameEventHandler.about();
        });

        // Preferenze
        preferencesMenuItem.setOnAction(e -> {
            gameEventHandler.userDidSomething();

            int currentBombs = DefaultPreferenceService.getInstance().getBombs();
            String currentLang = DefaultPreferenceService.getInstance().getLang();
            int maxBombs = game.getRows() * game.getCols() - 1;

            UiDialogs.showPreferencesDialog(
                    bundle,
                    currentBombs,
                    currentLang,
                    maxBombs,

                    (bombs, lang) -> {
                        DefaultPreferenceService.getInstance().setBombs(bombs);
                        DefaultPreferenceService.getInstance().setLang(lang);
                        UserFeedbackViewFxml.getInstance().showMessage(bundle.getString("prefs.saved"));
                    },

                    (msg) -> UserFeedbackViewFxml.getInstance().showMessageSticky(msg)
            );
        });

        // Esci
        quitMenuItem.setOnAction(e -> gameEventHandler.quit()

        );

        // all’avvio (prima di creare una partita), disabilitiamo “Save” e “Save As”
        disableSaveOptions();
    }



    public void disableSaveOptions() {
        saveMenuItem.setDisable(true);
        saveAsMenuItem.setDisable(true);
    }
    public void enableSaveOptions() {
        saveMenuItem.setDisable(false);
        saveAsMenuItem.setDisable(false);
    }
}
