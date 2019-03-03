package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;

import java.lang.reflect.Type;

public class EnchantmentSerializer implements JsonSerializer<Enchantment> {
    @Override
    public JsonElement serialize(Enchantment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject enchantment = new JsonObject();

        try {
            enchantment.addProperty("name", I18n.format(src.getName()));
            enchantment.addProperty("unlocalizedName", src.getName());
            enchantment.add("resourceLocation", context.serialize(Enchantment.REGISTRY.getNameForObject(src)));
            enchantment.addProperty("type", src.type != null ? src.type.toString() : null);
            enchantment.addProperty("rarity", src.getRarity().toString());
            enchantment.addProperty("minLevel", src.getMinLevel());
            enchantment.addProperty("maxLevel", src.getMaxLevel());
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Enchantments!");
            Probe.logger.error(e, e);
        }

        return enchantment;
    }
}
