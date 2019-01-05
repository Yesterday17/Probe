package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.ZSRCFile;
import com.google.gson.*;
import net.minecraftforge.fluids.Fluid;

import java.lang.reflect.Type;

public class ZSRCSerializer implements JsonSerializer<ZSRCFile> {
    @Override
    public JsonElement serialize(ZSRCFile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject zsrc = new JsonObject();

        zsrc.add("mcVersion", context.serialize((src.getMcVersion())));
        zsrc.add("forgeVersion", context.serialize(src.getForgeVersion()));

        zsrc.add("mods", context.serialize(src.getMods()));
        zsrc.add("items", context.serialize(src.getItems()));
        zsrc.add("enchantments", context.serialize(src.getEnchantments()));
        zsrc.add("entities", context.serialize(src.getEntities()));

        JsonArray fluids = new JsonArray();
        src.getFluids().forEach(fluid->fluids.add(context.serialize(fluid, Fluid.class)));
        zsrc.add("fluids", fluids);

        return zsrc;
    }
}
