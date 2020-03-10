package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import cn.yesterday17.probe.ZSRCFile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class JEIItemSerializer implements JsonSerializer<ZSRCFile.JEIItem> {
    @Override
    public JsonElement serialize(ZSRCFile.JEIItem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject ingredient = new JsonObject();
        ItemStack stack = src.getStack();

        try {
            ingredient.addProperty("name", src.getIngredient().getDisplayName());
            ingredient.addProperty("unlocalizedName", stack.getUnlocalizedName());
            ingredient.addProperty("modName", src.getIngredient().getModNameForSorting());
            ingredient.add("resourceLocation", context.serialize(stack.getItem().getRegistryName(), ResourceLocation.class));
            ingredient.addProperty("metadata", stack.getMetadata());

            ingredient.addProperty("maxStackSize", stack.getMaxStackSize());
            ingredient.addProperty("maxDamage", stack.getMaxDamage());
            ingredient.addProperty("canRepair", stack.getItem().isRepairable());
            ingredient.add("tooltips", context.serialize(src.getIngredient().getTooltipStrings()));
            ingredient.add("creativeTabStrings", context.serialize(src.getIngredient().getCreativeTabsStrings()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing JEIItems!");
            Probe.logger.error(e, e);
        }

        return ingredient;
    }
}
