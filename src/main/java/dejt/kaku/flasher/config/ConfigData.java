package dejt.kaku.flasher.config;

import java.util.Arrays;
import java.util.List;

/**
 * Default settings.
 */
public class ConfigData {

    public boolean allowChasingWithoutHit = false;
    public List<String> entityList_affected = Arrays.asList("Skeleton", "Creeper");
    public List<String> ports_available = Arrays.asList("COM1", "COM2", "COM3", "COM4", "COM5", "COM6");

    public Mechanics mechanics = new Mechanics();
    public BoardSettings settings = new BoardSettings();

    public static class Mechanics {

        public int checking_radius = 10;
        public boolean view = false;

    }

    public static class BoardSettings {

        public boolean noBoardAllowRun = true;
        public int default_pin = 13;
    }
}
