package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.FluidSerializer;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Fluid.class, new FluidSerializer())
            .addSerializationExclusionStrategy(new ExclusionStrategy() {
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
    })
            .serializeNulls()
            .setPrettyPrinting()
            .create();
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
            ZSRCFile.ItemEntry item = new ZSRCFile.ItemEntry(entry.getKey());
            item.setUnlocalizedName(entry.getValue().getUnlocalizedName(), ".name");
            rcFile.Items.add(item);
        });

        // Enchantments
        ForgeRegistries.ENCHANTMENTS.getEntries().forEach((entry)->{
            ZSRCFile.EnchantmentEntry enchantment = new ZSRCFile.EnchantmentEntry(entry.getKey());
            enchantment.setUnlocalizedName(entry.getValue().getName());
            enchantment.rarity = entry.getValue().getRarity().toString();
            enchantment.type = entry.getValue().type.toString();
            rcFile.Enchantments.add(enchantment);
        });

        // Entities
        ForgeRegistries.ENTITIES.getEntries().forEach((entry)->{
            ZSRCFile.EntityEntry entity = new ZSRCFile.EntityEntry(entry.getKey());
            entity.setUnlocalizedName(entry.getValue().getName());

            rcFile.Entities.add(entity);
        });

        // Liquids
        rcFile.Fluids.addAll(FluidRegistry.getRegisteredFluids().values());

        // Write to .zsrc
        try {
            BufferedWriter rcBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./scripts/.zsrc"), StandardCharsets.UTF_8
            ));
            gson.toJson(rcFile, rcBufferedWriter);
            rcBufferedWriter.close();
            logger.info("Probe loaded successfully!");
        } catch (Exception e) {
            logger.error("Probe met an error while loading! Please report to author about the problem!");
            logger.error(e.getMessage());
        }
    }
}
