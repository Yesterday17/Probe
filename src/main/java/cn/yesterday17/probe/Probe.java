package cn.yesterday17.probe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

@Mod(
        modid = Probe.MOD_ID,
        name = Probe.NAME,
        version = Probe.VERSION,
        clientSideOnly = true,
        dependencies = "required-after:crafttweaker"
)
public class Probe
{
    static final String MOD_ID = "probe";
    static final String NAME = "Probe";
    static final String VERSION = "1.0.0";

    private static Logger logger;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ZSRCFile rcFile = new ZSRCFile();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        // Items
        ForgeRegistries.ITEMS.getEntries().forEach((entry)->{
            ZSRCFile.ItemEntry item = new ZSRCFile.ItemEntry();
            item.domain = entry.getKey().getResourceDomain();
            item.path = entry.getKey().getResourcePath();
            item.unlocalizedName = entry.getValue().getUnlocalizedName();
            item.localizedName = I18n.format(item.unlocalizedName + ".name");
            rcFile.Items.add(item);
        });

        // Write to .zsrc
        try {
            FileWriter rcFileWriter = new FileWriter("./scripts/.zsrc");
            gson.toJson(rcFile, rcFileWriter);
            rcFileWriter.close();
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
