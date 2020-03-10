package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.ProbeConfig;
import cn.yesterday17.probe.ZSRCFile;
import com.google.gson.*;
import mezz.jei.gui.ingredients.IIngredientListElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.lang.reflect.Type;

public class ZSRCSerializer implements JsonSerializer<ZSRCFile> {
    @Override
    public JsonElement serialize(ZSRCFile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject zsrc = new JsonObject();

        zsrc.add("mcVersion", context.serialize((src.getMcVersion())));
        zsrc.add("forgeVersion", context.serialize(src.getForgeVersion()));
        zsrc.add("probeVersion", context.serialize((src.getProbeVersion())));

        // Config
        JsonObject config = new JsonObject();
        config.addProperty("mods", ProbeConfig.enableMods);
        config.addProperty("items", ProbeConfig.enableItems);
        config.addProperty("enchantments", ProbeConfig.enableEnchantments);
        config.addProperty("entities", ProbeConfig.enableEntities);
        config.addProperty("fluids", ProbeConfig.enableFluids);
        config.addProperty("oredictionary", ProbeConfig.enableOreDictionary);
        config.addProperty("zentype", ProbeConfig.enableZenType);
        config.addProperty("zenpackage", ProbeConfig.enableZenPackage);
        config.addProperty("globals", ProbeConfig.enableGlobals);
        zsrc.add("config", config);

        // Mods
        JsonArray mods = new JsonArray();
        if (ProbeConfig.enableMods) {
            src.getMods().forEach(mod -> mods.add(context.serialize(mod, ModContainer.class)));
        }
        zsrc.add("mods", mods);

        // Items
        JsonArray items = new JsonArray();
        if (ProbeConfig.enableItems) {
            src.getJEIItems().forEach(item -> items.add(context.serialize(item, IIngredientListElement.class)));
        }
        zsrc.add("items", items);

        // Enchantments
        JsonArray enchantments = new JsonArray();
        if (ProbeConfig.enableEnchantments) {
            src.getEnchantments().forEach(enchantment -> enchantments.add(context.serialize(enchantment, Enchantment.class)));
        }
        zsrc.add("enchantments", enchantments);

        // Entitles
        JsonArray entities = new JsonArray();
        if (ProbeConfig.enableEntities) {
            src.getEntities().forEach(entity -> entities.add(context.serialize(entity, EntityEntry.class)));
        }
        zsrc.add("entities", entities);

        // Fluids
        JsonArray fluids = new JsonArray();
        if (ProbeConfig.enableFluids) {
            src.getFluids().forEach(fluid -> fluids.add(context.serialize(fluid, Fluid.class)));
        }
        zsrc.add("fluids", fluids);

        // OreDictionary
        JsonArray oreDictionary = new JsonArray();
        if (ProbeConfig.enableOreDictionary) {
            src.getOreDictionary().forEach(od -> oreDictionary.add(context.serialize(od, String.class)));
        }
        zsrc.add("oredictionary", oreDictionary);

        // ZenTypes
        JsonArray zenType = new JsonArray();
        if (ProbeConfig.enableZenType) {
            src.getZenType().forEach(t -> zenType.add(context.serialize(t, String.class)));
        }
        zsrc.add("zentype", zenType);

        JsonObject zenPackage = new JsonObject();
        if (ProbeConfig.enableZenType) {
            src.getZenPackages().forEach((name, type) -> zenPackage.add(name, context.serialize(type)));
        }
        zsrc.add("zenpackage", zenPackage);

        // CraftTweaker Globals
        JsonObject globals = new JsonObject();
        if (ProbeConfig.enableGlobals) {
            src.getGlobalFields().forEach(globals::addProperty);
            src.getGlobalMethods().forEach((key, value) -> globals.add(key, context.serialize(value, IJavaMethod.class)));
            src.getGlobalGetters().forEach((key, value) -> globals.add(key, context.serialize(value, IJavaMethod.class)));
        }
        zsrc.add("globals", globals);

        return zsrc;
    }
}
