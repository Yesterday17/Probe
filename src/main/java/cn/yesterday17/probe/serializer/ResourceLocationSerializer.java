package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class ResourceLocationSerializer implements JsonSerializer<ResourceLocation> {
    @Override
    public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject resource = new JsonObject();

        try {
            resource.addProperty("domain", src.getResourceDomain());
            resource.addProperty("path", src.getResourcePath());
        } catch (Exception e) {
            Probe.logger.error("Failed serializing ResourceLocation!");
            Probe.logger.error(e, e);
        }

        return resource;
    }
}
