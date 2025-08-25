package ch.supsi.minesweeper.service;

public interface PreferenceService {
    int getBombs();
    void setBombs(int bombs);
    String getLang();
    void setLang(String lang);
    void reload();
}