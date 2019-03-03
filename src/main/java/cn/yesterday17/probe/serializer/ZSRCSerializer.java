package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.ProbeConfig;
import cn.yesterday17.probe.ZSRCFile;
import com.google.gson.*;
import mezz.jei.gui.ingredients.IIngredientListElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.EntityEntry;

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
        zsrc.add("config", config);

        if (ProbeConfig.enableMods)
            zsrc.add("mods", context.serialize(src.getMods()));

        // Items
        JsonArray items = new JsonArray();
        if (ProbeConfig.enableItems) {
            // src.getItems().forEach(item -> items.add(context.serialize(item, Item.class)));
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
        if (ProbeConfig.enableOreDictionary)
            zsrc.add("oredictionary", context.serialize(src.getOreDictionary()));

        return zsrc;
    }
}
