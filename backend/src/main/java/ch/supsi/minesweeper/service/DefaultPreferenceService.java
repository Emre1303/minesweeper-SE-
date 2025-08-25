package ch.supsi.minesweeper.service;

import ch.supsi.minesweeper.model.Preferences;
import ch.supsi.minesweeper.persistence.AppPreferenceRepository;
import ch.supsi.minesweeper.persistence.PreferenceRepository;

public class DefaultPreferenceService implements PreferenceService {
    private static DefaultPreferenceService myself;
    private final PreferenceRepository repo;
    private Preferences cache;


    public DefaultPreferenceService() {
        this.repo = new AppPreferenceRepository();
        this.cache = repo.load();
    }

    public static DefaultPreferenceService getInstance() {
        if (myself == null) myself = new DefaultPreferenceService();
        return myself;
    }



    @Override public int getBombs() { return cache.getBombs(); }

    @Override public void setBombs(int bombs) {
        if (bombs < 1) throw new IllegalArgumentException("bombs must be >= 1");
        cache.setBombs(bombs);
        repo.save(cache);
    }

    @Override public String getLang() { return cache.getLang(); }

    @Override public void setLang(String lang) {
        if (lang == null || lang.isBlank()) throw new IllegalArgumentException("lang required");
        cache.setLang(lang);
        repo.save(cache);
    }

}