package ch.supsi.minesweeper.persistence;

import ch.supsi.minesweeper.model.GameModel;
import java.nio.file.Path;

public interface GameRepository {
    void save(GameModel model, Path path);
    void load(GameModel model, Path path);
}