package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.*;
import com.google.gson.GsonBuilder;
import crafttweaker.zenscript.GlobalRegistry;
import mezz.jei.Internal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityList;
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
import stanhebben.zenscript.symbols.*;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    static final String VERSION = "@VERSION@";

    public static Logger logger;
    private static GsonBuilder gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ArtifactVersion.class, new ArtifactVersionSerializer())
            .registerTypeHierarchyAdapter(ResourceLocation.class, new ResourceLocationSerializer())
            .registerTypeHierarchyAdapter(CreativeTabs.class, new CreativeTabSerializer())
            .registerTypeHierarchyAdapter(ModContainer.class, new ModSerializer())
            .registerTypeHierarchyAdapter(ZSRCFile.JEIItem.class, new JEIItemSerializer())
            .registerTypeHierarchyAdapter(Enchantment.class, new EnchantmentSerializer())
            .registerTypeHierarchyAdapter(EntityEntry.class, new EntitySerializer())
            .registerTypeHierarchyAdapter(Fluid.class, new FluidSerializer())
            .registerTypeHierarchyAdapter(IJavaMethod.class, new IJavaMethodSerializer())
            .registerTypeHierarchyAdapter(ZenTypeNative.class, new ZenTypeNativeSerializer())

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
        rcFile.Mods = Loader.instance().getIndexedModList().values().stream()
                .filter(s -> s instanceof FMLModContainer)
                .sorted(Comparator.comparing(ModContainer::getModId))
                .collect(Collectors.toList());

        // Items
        if (Internal.getRuntime() != null) {
            rcFile.JEIItems = Internal.getRuntime().getIngredientFilter().getIngredientList().stream()
                    .filter(e -> e.getIngredient() instanceof ItemStack)
                    .map(ZSRCFile.JEIItem::new)
                    .sorted(Comparator.comparing(ZSRCFile.JEIItem::getSortName))
                    .collect(Collectors.toList());
        }

        // Enchantments
        rcFile.Enchantments = ForgeRegistries.ENCHANTMENTS.getValuesCollection().stream()
                .sorted(Comparator.comparing(Enchantment::getName))
                .collect(Collectors.toList());

        // Entities
        rcFile.Entities = ForgeRegistries.ENTITIES.getValuesCollection().stream()
                .sorted(Comparator.comparing(e -> EntityList.getID(e.getEntityClass())))
                .collect(Collectors.toList());

        // Fluids
        rcFile.Fluids = FluidRegistry.getRegisteredFluids().values().stream()
                .sorted(Comparator.comparing(f -> FluidRegistry.getRegisteredFluidIDs().get(f)))
                .collect(Collectors.toList());

        // OreDictionary
        rcFile.OreDictionary = Arrays.stream(OreDictionary.getOreNames())
                .sorted()
                .collect(Collectors.toList());

        // ZenType
        rcFile.ZenType = GlobalRegistry.getTypes().getTypeMap().values().stream()
                .map(ZenType::getName)
                .filter(name -> !name.endsWith("?"))
                .sorted()
                .collect(Collectors.toList());

        // ZenPackages
        rcFile.ZenPackages = getZenTypes(GlobalRegistry.getRoot());

        // CraftTweaker Globals
        GlobalRegistry.getGlobals().forEach((key, value) -> {
            if (value instanceof SymbolJavaStaticMethod) {
                IJavaMethod r = (IJavaMethod) getField(SymbolJavaStaticMethod.class, value, "method");
                rcFile.GlobalMethods.put(key, r);
            } else if (value instanceof SymbolJavaStaticField) {
                Field f = (Field) getField(SymbolJavaStaticField.class, value, "field");
                rcFile.GlobalFields.put(key, f.getType().getName());
            } else if (value instanceof SymbolJavaStaticGetter) {
                IJavaMethod r = (IJavaMethod) getField(SymbolJavaStaticGetter.class, value, "method");
                rcFile.GlobalGetters.put(key, r);
            }
        });

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


    private static Map<String, ZenType> getZenTypes(SymbolPackage primer) {
        Map<String, ZenType> result = new HashMap<>();

        primer.getPackages().forEach((str, symbol) -> {
            if (symbol instanceof SymbolPackage) {
                result.putAll(getZenTypes((SymbolPackage) symbol));
            } else if (symbol instanceof SymbolType || symbol instanceof ZenTypeNative) {
                ZenTypeNative typeNative = null;
                if (symbol instanceof SymbolType) {
                    ZenType type = ((SymbolType) symbol).getType();
                    if (type instanceof ZenTypeNative) {
                        typeNative = (ZenTypeNative) type;
                    }
                } else {
                    typeNative = (ZenTypeNative) symbol;
                }

                if (typeNative != null) {
                    result.put(typeNative.getName(), typeNative);
                }
            }
        });

        return result;
    }

    private static <T> Object getField(Class<T> cls, Object object, String key) {
        try {
            Field f = cls.getDeclaredField(key);
            f.setAccessible(true);
            return f.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}