package ch.supsi.minesweeper.persistence;

import ch.supsi.minesweeper.model.Preferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Properties;

public class AppPreferenceRepository implements PreferenceRepository {

    private static final String FILE_NAME = "config.properties";
    private static final String KEY_BOMBS = "bombs";
    private static final String KEY_LANG  = "lang";
    private static final int    DEF_BOMBS = 10;
    private static final String DEF_LANG  = "en";

    private final Path userFile = Paths.get(System.getProperty("user.home"), ".minesweeper", FILE_NAME);
    private final Properties props = new Properties();

    public AppPreferenceRepository() { loadProps(); }

    private void loadProps() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (in != null) props.load(in);
        } catch (IOException ignore) {}

        if (Files.exists(userFile)) {
            try (InputStream in = Files.newInputStream(userFile)) {
                props.load(in);
            } catch (IOException ignore) {}
        }
    }

    @Override
    public Preferences load() {
        int bombs = Integer.parseInt(props.getProperty(KEY_BOMBS, String.valueOf(DEF_BOMBS)));
        String lang = props.getProperty(KEY_LANG, DEF_LANG);
        return new Preferences(bombs, lang);
    }

    @Override
    public void save(Preferences prefs) {
        props.setProperty(KEY_BOMBS, String.valueOf(prefs.getBombs()));
        props.setProperty(KEY_LANG, prefs.getLang());
        try {
            Files.createDirectories(userFile.getParent());
            try (OutputStream out = Files.newOutputStream(userFile)) {
                props.store(out, "Minesweeper preferences");
            }
        } catch (IOException ignore) {}
    }
}