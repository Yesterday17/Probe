package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
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
            .registerTypeAdapter(ArtifactVersion.class, new ArtifactVersionSerializer())
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationSerializer())
            .registerTypeAdapter(ModMetadata.class, new ModSerializer())
            .registerTypeHierarchyAdapter(Item.class, new ItemSerializer())
            .registerTypeHierarchyAdapter(Enchantment.class, new EnchantmentSerializer())
            .registerTypeHierarchyAdapter(EntityEntry.class, new EntitySerializer())
            .registerTypeHierarchyAdapter(Fluid.class, new FluidSerializer())
            .serializeNulls().setPrettyPrinting()
            .create();
    private static ZSRCFile rcFile = new ZSRCFile();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        rcFile.mcVersion = ForgeVersion.mcVersion;
        rcFile.forgeVersion = ForgeVersion.getVersion();
        Loader.instance().getIndexedModList().forEach((modid, container) -> {
            if (container instanceof FMLModContainer) {
                rcFile.Mods.add(container.getMetadata());
            }
        });
        rcFile.Items.addAll(ForgeRegistries.ITEMS.getValuesCollection());
        rcFile.Enchantments.addAll(ForgeRegistries.ENCHANTMENTS.getValuesCollection());
        rcFile.Entities.addAll(ForgeRegistries.ENTITIES.getValuesCollection());
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
