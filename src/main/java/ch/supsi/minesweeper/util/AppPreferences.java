package ch.supsi.minesweeper.util;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public final class AppPreferences {


    private static final String CLASSPATH_FILE = "/config.properties"; // dentro il jar
    private static final String USER_FILE      = "config.properties";  // nella cwd
    private static final String KEY_BOMBS      = "bombs";
    private static final String KEY_LANG       = "lang";
    private static final int    DEF_BOMBS      = 10;
    private static final String DEF_LANG       = "en";
    private static final Properties props = new Properties();

    static {

        try (InputStream in = AppPreferences.class.getResourceAsStream(CLASSPATH_FILE)) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            System.err.println("Impossibile leggere " + CLASSPATH_FILE + ": " + e.getMessage());
        }

        Path userPath = Paths.get(USER_FILE);
        if (Files.exists(userPath)) try (InputStream in = Files.newInputStream(userPath)) {
            props.load(in);       // sovrascrive i valori di default
        } catch (IOException e) {
            System.err.println("Impossibile leggere " + USER_FILE + ": " + e.getMessage());
        }
    }


    public static void save() {
        try (OutputStream out = Files.newOutputStream(Paths.get(USER_FILE))) {
            props.store(out, "Minesweeper preferences – cold reload");
        } catch (IOException e) {
            System.err.println("Impossibile scrivere " + USER_FILE + ": " + e.getMessage());
        }
    }


    public static int    getBombs() { return getInt(KEY_BOMBS, DEF_BOMBS); }
    public static String getLang()  { return props.getProperty(KEY_LANG, DEF_LANG); }

    public static void setBombs(int bombs) { props.setProperty(KEY_BOMBS, String.valueOf(bombs)); }
    public static void setLang(String lang) { props.setProperty(KEY_LANG, lang); }


    private static int getInt(String key, int defVal) {
        try { return Integer.parseInt(props.getProperty(key, String.valueOf(defVal))); }
        catch (NumberFormatException nfe) { return defVal; }
    }

    private AppPreferences() { }
}