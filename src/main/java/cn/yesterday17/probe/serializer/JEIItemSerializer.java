package cn.yesterday17.probe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import mezz.jei.gui.ingredients.IIngredientListElement;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class JEIItemSerializer implements JsonSerializer<IIngredientListElement> {
    @Override
    public JsonElement serialize(IIngredientListElement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject ingredient = new JsonObject();
        ItemStack stack = (ItemStack) src.getIngredient();

        ingredient.addProperty("name", src.getDisplayName());
        ingredient.addProperty("unlocalizedName", stack.getUnlocalizedName());
        ingredient.addProperty("modName", src.getModNameForSorting());
        ingredient.add("resourceLocation", context.serialize(stack.getItem().getRegistryName(), ResourceLocation.class));
        ingredient.addProperty("metadata", stack.getMetadata());

        ingredient.addProperty("maxStackSize", stack.getMaxStackSize());
        ingredient.addProperty("maxDamage", stack.getMaxDamage());
        ingredient.addProperty("canRepair", stack.getItem().isRepairable());
        ingredient.add("tooltips", context.serialize(src.getTooltipStrings()));
        ingredient.add("creativeTabStrings", context.serialize(src.getCreativeTabsStrings()));

        return ingredient;
    }
}
