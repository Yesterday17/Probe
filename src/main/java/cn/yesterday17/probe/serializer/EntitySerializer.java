package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.lang.reflect.Type;

public class EntitySerializer implements JsonSerializer<EntityEntry> {
    @Override
    public JsonElement serialize(EntityEntry src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject entity = new JsonObject();

        try {
            entity.addProperty("id", EntityList.getID(src.getEntityClass()));
            entity.addProperty("name", src.getName());
            entity.add("resourceLocation", context.serialize(EntityList.getKey(src.getEntityClass())));
            // Dump information about Spawn eggs here if necessary.
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Entities!");
            Probe.logger.error(e, e);
        }
        return entity;
    }
}
