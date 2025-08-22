package ch.supsi.minesweeper.util;

import java.io.IOException;
import java.util.Properties;

public class BuildInfo {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(BuildInfo.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load app info", e);
        }
    }

    public static String getName()        { return props.getProperty("app.name", "Minesweeper"); }
    public static String getVersion()     { return props.getProperty("app.version", "0.0.1"); }
    public static String getDescription() { return props.getProperty("app.description", ""); }
    public static String getAuthor()      { return props.getProperty("app.author", ""); }
    public static String buildDate()      { return props.getProperty("app.buildDate", "unknown"); }
}