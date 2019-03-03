package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraftforge.fml.common.ModMetadata;

import java.lang.reflect.Type;

public class ModSerializer implements JsonSerializer<ModMetadata> {
    @Override
    public JsonElement serialize(ModMetadata src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject mod = new JsonObject();

        try {
            mod.addProperty("modid", src.modId);
            mod.addProperty("name", src.name);
            mod.addProperty("description", src.description);
            mod.addProperty("url", src.url);
            // mod.addProperty("logoFile", src.logoFile);
            mod.addProperty("version", src.version);
            mod.add("authorList", context.serialize(src.authorList));
            mod.addProperty("credits", src.credits);
            // mod.add("screenshots", context.serialize(src.screenshots));
            mod.add("parentMod", context.serialize(src.parentMod));
            mod.add("childMods", context.serialize(src.childMods));
            mod.add("requiredMods", context.serialize(src.requiredMods));
            mod.add("dependencies", context.serialize(src.dependencies));
            mod.add("dependants", context.serialize(src.dependants));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Mods!");
            Probe.logger.error(e, e);
        }

        return mod;
    }
}
