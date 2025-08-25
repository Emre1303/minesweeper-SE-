package ch.supsi.minesweeper.persistence;

import ch.supsi.minesweeper.model.Preferences;

public interface PreferenceRepository {
    Preferences load();
    void save(Preferences prefs);
}