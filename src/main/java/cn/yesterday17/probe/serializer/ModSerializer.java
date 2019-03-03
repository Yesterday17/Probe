package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraftforge.fml.common.ModContainer;

import java.lang.reflect.Type;

public class ModSerializer implements JsonSerializer<ModContainer> {
    @Override
    public JsonElement serialize(ModContainer src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject mod = new JsonObject();

        try {
            mod.addProperty("modid", src.getModId());
            mod.addProperty("name", src.getName());
            mod.addProperty("description", src.getMetadata().description);
            mod.addProperty("url", src.getMetadata().url);
            // mod.addProperty("logoFile", src.logoFile);
            mod.addProperty("version", src.getDisplayVersion());
            mod.add("authorList", context.serialize(src.getMetadata().authorList));
            mod.addProperty("credits", src.getMetadata().credits);
            // mod.add("screenshots", context.serialize(src.screenshots));
            // mod.add("parentMod", context.serialize(src.getMetadata().parentMod, SimpleModSerializer.class));
            // mod.add("childMods", context.serialize(src.getMetadata().childMods));
            mod.add("requiredMods", context.serialize(src.getRequirements()));
            mod.add("dependencies", context.serialize(src.getDependencies()));
            mod.add("dependants", context.serialize(src.getDependants()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Mods!");
            Probe.logger.error(e, e);
        }

        return mod;
    }
}
