package ch.supsi.minesweeper.model;

import java.io.IOException;
import java.nio.file.Path;


public interface GamePersistence {
    void save(GameModel model, Path path) throws IOException;
    void load(GameModel model, Path path) throws IOException;
}