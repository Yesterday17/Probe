package cn.yesterday17.probe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class ItemSerializer implements JsonSerializer<Item> {
    @Override
    public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject item = new JsonObject();

        item.addProperty("id", Item.getIdFromItem(src));
        item.addProperty("name", ("" + I18n.format(src.getUnlocalizedName() + ".name")).trim());
        item.addProperty("unlocalizedName", src.getUnlocalizedName());
        item.add("resourceLocation", context.serialize(Item.REGISTRY.getNameForObject(src), ResourceLocation.class));
        item.addProperty("maxStackSize", src.getItemStackLimit());
        item.addProperty("maxDamage", src.getMaxDamage());
        item.addProperty("bFull3D", src.isFull3D());
        item.addProperty("hasSubtypes", src.getHasSubtypes());
        item.addProperty("canRepair", src.isRepairable());

        item.add("containerItem", context.serialize(src.getContainerItem()));
        item.add("tabToDisplayOn", context.serialize(src.getCreativeTab()));
        return item;
    }
}
