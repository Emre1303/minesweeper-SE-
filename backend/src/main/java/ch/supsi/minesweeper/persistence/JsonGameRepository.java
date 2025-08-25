package ch.supsi.minesweeper.persistence;

import ch.supsi.minesweeper.model.GameModel;
import ch.supsi.minesweeper.model.GameStateJson;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;

public class JsonGameRepository implements GameRepository {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void save(GameModel model, Path path) {
        try {
            GameStateJson state = new GameStateJson(model);
            MAPPER.writeValue(path.toFile(), state);
        } catch (IOException e) {
            throw new RuntimeException("Error saving game", e);
        }
    }

    @Override
    public void load(GameModel model, Path path) {
        try {
            GameStateJson state = MAPPER.readValue(path.toFile(), GameStateJson.class);
            model.loadFromState(state);
            model.markStarted();
        } catch (IOException e) {
            throw new RuntimeException("Error loading game", e);
        }
    }
}