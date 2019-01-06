package cn.yesterday17.probe.serializer;

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
        resource.addProperty("domain", src.getResourceDomain());
        resource.addProperty("path", src.getResourcePath());
        return resource;
    }
}
