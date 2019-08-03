package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.*;
import cn.yesterday17.probe.serializer.CTRegistries.MethodSerializer;
import cn.yesterday17.probe.serializer.CTRegistries.ZenTypeSerializer;
import com.google.gson.GsonBuilder;
import crafttweaker.zenscript.GlobalRegistry;
import mezz.jei.Internal;
import mezz.jei.gui.ingredients.IIngredientListElement;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import stanhebben.zenscript.dump.IDumpable;
import stanhebben.zenscript.dump.types.DumpDummy;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolPackage;
import stanhebben.zenscript.symbols.SymbolType;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mod(
        modid = Probe.MOD_ID,
        name = Probe.NAME,
        version = Probe.VERSION,
        clientSideOnly = true,
        dependencies = "required-after:crafttweaker" + ";" +
                "required-after:jei" + ";"
)
public class Probe {
    static final String MOD_ID = "probe";
    static final String NAME = "Probe";
    static final String VERSION = "0.1.21";

    public static Logger logger;
    private static GsonBuilder gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ArtifactVersion.class, new ArtifactVersionSerializer())
            .registerTypeHierarchyAdapter(ResourceLocation.class, new ResourceLocationSerializer())
            .registerTypeHierarchyAdapter(CreativeTabs.class, new CreativeTabSerializer())
            .registerTypeHierarchyAdapter(ModContainer.class, new ModSerializer())
            // .registerTypeHierarchyAdapter(Item.class, new ItemSerializer())
            .registerTypeHierarchyAdapter(IIngredientListElement.class, new JEIItemSerializer())
            .registerTypeHierarchyAdapter(Enchantment.class, new EnchantmentSerializer())
            .registerTypeHierarchyAdapter(EntityEntry.class, new EntitySerializer())
            .registerTypeHierarchyAdapter(Fluid.class, new FluidSerializer())
            .registerTypeHierarchyAdapter(IJavaMethod.class, new MethodSerializer())
            .registerTypeHierarchyAdapter(ZenType.class, new ZenTypeSerializer())

            .serializeNulls();
    private static ZSRCFile rcFile = new ZSRCFile();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        rcFile.mcVersion = ForgeVersion.mcVersion;
        rcFile.forgeVersion = ForgeVersion.getVersion();
        rcFile.probeVersion = VERSION;

        // Mods
        Loader.instance().getIndexedModList().forEach((modid, container) -> {
            if (container instanceof FMLModContainer) {
                rcFile.Mods.add(container);
            }
        });

        // Items
        Internal.getRuntime().getIngredientFilter().getIngredientList().forEach((element -> {
            if (element.getIngredient() instanceof ItemStack) {
                rcFile.JEIItems.add(element);
            }
        }));
        // remain for compatibility
        // rcFile.Items.addAll(ForgeRegistries.ITEMS.getValuesCollection());

        // Enchantments
        rcFile.Enchantments.addAll(ForgeRegistries.ENCHANTMENTS.getValuesCollection());

        // Entities
        rcFile.Entities.addAll(ForgeRegistries.ENTITIES.getValuesCollection());

        // Fluids
        rcFile.Fluids.addAll(FluidRegistry.getRegisteredFluids().values());

        // OreDictionary
        Collections.addAll(rcFile.OreDictionary, OreDictionary.getOreNames());


        //Global Registries
        rcFile.zenTypes.addAll(recursive(GlobalRegistry.getRoot()));

        // Write to .zsrc
        try {
            BufferedWriter rcBufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("./scripts/.zsrc"), StandardCharsets.UTF_8
            ));
            if (ProbeConfig.setPrettyPrinting)
                gson.setPrettyPrinting();
            gson.create().toJson(rcFile, rcBufferedWriter);
            rcBufferedWriter.close();
            logger.info("Probe loaded successfully!");
        } catch (Exception e) {
            logger.error("Probe met an error while loading! Please report to author about the problem!");
            logger.error(e, e);
        }

    }


    private static List<ZenType> recursive(SymbolPackage primer) {
        List<ZenType> result = new ArrayList<>();

        primer.getPackages().forEach((str, symbol) -> {
            if (symbol instanceof SymbolPackage)
                result.addAll(recursive((SymbolPackage) symbol));
            else if (symbol instanceof SymbolType) if
            (((SymbolType) symbol).getType() != null)
                result.add(((SymbolType) symbol).getType());
        });

        return result;
    }
}