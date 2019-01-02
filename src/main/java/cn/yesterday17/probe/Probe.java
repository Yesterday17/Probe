package cn.yesterday17.probe;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Mod(
        modid = Probe.MOD_ID,
        name = Probe.NAME,
        version = Probe.VERSION,
        clientSideOnly = true,
        dependencies = "required-after:crafttweaker"
)
public class Probe {
    static final String MOD_ID = "probe";
    static final String NAME = "Probe";
    static final String VERSION = "1.0.0";

    private static Logger logger;
    private static Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().equals("requiredMods")
                    || f.getName().equals("dependencies")
                    || f.getName().equals("dependants");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).setPrettyPrinting().create();
    private static ZSRCFile rcFile = new ZSRCFile();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        // Version
        rcFile.mcVersion = ForgeVersion.mcVersion;
        rcFile.forgeVersion = ForgeVersion.getVersion();

        // Mods
        Loader.instance().getIndexedModList().forEach((modid, container) -> {
            if (container instanceof FMLModContainer) {
                rcFile.Mods.add(container.getMetadata());
            }
        });

        // Items
        ForgeRegistries.ITEMS.getEntries().forEach((entry) -> {
            ZSRCFile.ItemEntry item = new ZSRCFile.ItemEntry();
            item.domain = entry.getKey().getResourceDomain();
            item.path = entry.getKey().getResourcePath();
            item.unlocalizedName = entry.getValue().getUnlocalizedName();
            item.localizedName = I18n.format(item.unlocalizedName + ".name");
            rcFile.Items.add(item);
        });

        ForgeRegistries.ENCHANTMENTS.getEntries().forEach((entry)->{
            ZSRCFile.EnchantmentEntry enchantment = new ZSRCFile.EnchantmentEntry();
            enchantment.domain = entry.getKey().getResourceDomain();
            enchantment.path = entry.getKey().getResourcePath();
            enchantment.unlocalizedName = entry.getValue().getName();
            enchantment.localizedName = I18n.format(enchantment.unlocalizedName);

            enchantment.rarity = entry.getValue().getRarity();
            enchantment.type = entry.getValue().type;
            rcFile.Enchantments.add(enchantment);
        });

        // Write to .zsrc
        try {
            BufferedWriter rcBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./scripts/.zsrc"), StandardCharsets.UTF_8
            ));
            gson.toJson(rcFile, rcBufferedWriter);
            rcBufferedWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
