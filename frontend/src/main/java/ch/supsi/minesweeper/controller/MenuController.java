package ch.supsi.minesweeper.controller;

import ch.supsi.minesweeper.service.GameService;
import ch.supsi.minesweeper.util.BuildInfo;
import ch.supsi.minesweeper.view.DataView;
import ch.supsi.minesweeper.view.MenuBarViewFxml;
import ch.supsi.minesweeper.view.UiDialogs;
import ch.supsi.minesweeper.view.UserFeedbackViewFxml;
import javafx.application.Platform;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

class MenuController {

    private final GameService gameService;
    private final ResourceBundle bundle;
    private final int defaultBombs;

    private List<DataView> views;
    private Path currentFile;

    MenuController(GameService gameService,
                   ResourceBundle bundle,
                   int defaultBombs) {
        this.gameService = gameService;
        this.bundle      = bundle;
        this.defaultBombs = defaultBombs;
    }

    void initialize(List<DataView> views) { this.views = views; }

    private ResourceBundle rb() { return bundle; }


    void newGame() {
        Platform.runLater(() -> {
            int max   = gameService.getRows() * gameService.getCols() - 1;
            int bombs = Math.max(1, Math.min(defaultBombs, max));

            gameService.setMines(bombs);
            gameService.newGame();

            UserFeedbackViewFxml.getInstance().clearMessage();

            if (views != null) views.forEach(DataView::update);

            MenuBarViewFxml.getInstance().enableSaveOptions();

            UserFeedbackViewFxml.getInstance().showMessage(MessageFormat.format(rb().getString("dialog.new.body"), bombs)
            );
        });
    }

    void save() {
        if (currentFile == null) { saveAs(); return; }
        try {
            gameService.save(currentFile);
            if (views != null) views.forEach(DataView::update);
            Platform.runLater(() -> {
                UserFeedbackViewFxml.getInstance().showMessage(rb().getString("dialog.save.success"));
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                UserFeedbackViewFxml.getInstance().showMessage(rb().getString("dialog.save.error"));
            });
        }
    }

    void saveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(rb().getString("menu.file.saveas"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = chooser.showSaveDialog(null);
        if (file != null) { currentFile = file.toPath(); save(); }
    }

    void load() {
        if (currentFile == null) { open(); return; }
        try {
            gameService.load(currentFile);
            if (views != null) views.forEach(DataView::update);
            MenuBarViewFxml.getInstance().enableSaveOptions();
            Platform.runLater(() -> {
                UserFeedbackViewFxml.getInstance().showMessage(rb().getString("dialog.load.success"));
            });
        } catch (RuntimeException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                UserFeedbackViewFxml.getInstance().showMessage(rb().getString("dialog.load.error"));
            });
        }
    }

    void open() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(rb().getString("menu.file.open"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = chooser.showOpenDialog(null);
        if (file != null) { currentFile = file.toPath(); load(); }
    }

    void help()  {
        Platform.runLater(() -> {
            String title   = rb().getString("help.title");
            String header  = rb().getString("help.header");
            String content = rb().getString("help.content");

            String fullMsg = title + "\n" + header + ":\n" + content;

            UserFeedbackViewFxml.getInstance().showMessageSticky(fullMsg);
        });
    }

    void about() {
        Platform.runLater(() -> {
            UiDialogs.about(
                    BuildInfo.getName(),
                    BuildInfo.getVersion(),
                    BuildInfo.getDescription(),
                    BuildInfo.getAuthor(),
                    BuildInfo.buildDate()
            );
        });
    }

    void win() {
        Platform.runLater(() -> {
            MenuBarViewFxml.getInstance().disableSaveOptions();
            UserFeedbackViewFxml.getInstance().showMessageSticky(
                    rb().getString("alert.win.title") + "\n" + rb().getString("alert.win.text")
            );
        });
    }

    void lose() {
        Platform.runLater(() -> {
            MenuBarViewFxml.getInstance().disableSaveOptions();
            UserFeedbackViewFxml.getInstance().showMessageSticky(
                    rb().getString("alert.lose.title") + ": " +
                            rb().getString("alert.lose.header") + " — " +
                            rb().getString("alert.lose.text")
            );
        });
    }
}