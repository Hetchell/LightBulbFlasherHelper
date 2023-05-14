package dejt.kaku.flasher.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dejt.kaku.flasher.LightFlasher;

import java.io.*;
import java.lang.reflect.Constructor;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Code reference from SuperCoder7979's SimplexTerrain mod.
 */
public class ConfigHelper {

    public static final Gson GSON_bld = new GsonBuilder().setPrettyPrinting().create();


    public static ConfigData init(){
        ConfigData config = null;
        try {
            Path configpath = Paths.get("", "config", LightFlasher.MODID + ".json");
            if(Files.exists(configpath)){
                Reader reader = Files.newBufferedReader(configpath);
                config = GSON_bld.fromJson(reader, ConfigData.class);
                BufferedWriter writerTo = Files.newBufferedWriter(configpath);
                writerTo.write(GSON_bld.toJson(config));
                writerTo.close();
            } else {
                //make the json.
                config = new ConfigData();
                boolean success = Paths.get("", "config").toFile().mkdirs();
                if(success) System.out.println("Directory has been created");
                BufferedWriter writerTo = Files.newBufferedWriter(configpath);
                writerTo.write(GSON_bld.toJson(config));
                writerTo.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
}
