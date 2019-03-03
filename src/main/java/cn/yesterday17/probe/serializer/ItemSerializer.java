package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

@Deprecated
public class ItemSerializer implements JsonSerializer<Item> {
    @Override
    public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject item = new JsonObject();

        try {
            item.addProperty("id", Item.getIdFromItem(src));
            item.addProperty("name", ("" + I18n.format(src.getUnlocalizedName() + ".name")).trim());
            item.addProperty("unlocalizedName", src.getUnlocalizedName());
            item.add("resourceLocation", context.serialize(Item.REGISTRY.getNameForObject(src), ResourceLocation.class));
            item.addProperty("maxStackSize", new ItemStack(src).getMaxStackSize());
            item.addProperty("maxDamage", new ItemStack(src).getMaxDamage());
            item.addProperty("bFull3D", src.isFull3D());
            item.addProperty("hasSubtypes", src.getHasSubtypes());
            item.addProperty("canRepair", src.isRepairable());

            item.addProperty("containerItem", src.getContainerItem() == null ? null : Item.getIdFromItem(src.getContainerItem()));
            item.add("tabToDisplayOn", context.serialize(src.getCreativeTab() == null ? null : src.getCreativeTab().getTabIndex()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Items(Deprecated)!");
            Probe.logger.error(e, e);
        }

        return item;
    }
}
