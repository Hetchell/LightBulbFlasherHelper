package dejt.kaku.flasher;

import dejt.kaku.flasher.config.ConfigData;
import dejt.kaku.flasher.config.ConfigHelper;
import dejt.kaku.flasher.flasher.SetupBoard;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Mod(modid = LightFlasher.MODID, name = LightFlasher.NAME, version = LightFlasher.VERSION)
public class LightFlasher
{
    public static final String MODID = "lightbulbflasher";
    public static final String NAME = "Light Bulb Flasher helper";
    public static final String VERSION = "1.0";

    public static final ConfigData configuration = ConfigHelper.init();
    public static final SetupBoard board;

    static {
        try {
            board = new SetupBoard(configuration.settings.default_pin);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Logger logger;
    public static boolean finalStatus;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        finalStatus = board.getStatus() && board.getBoard() != null && board.getDefpin() != null;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
