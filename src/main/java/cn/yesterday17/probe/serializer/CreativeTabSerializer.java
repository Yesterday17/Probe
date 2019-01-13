package cn.yesterday17.probe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.lang.reflect.Type;

public class CreativeTabSerializer implements JsonSerializer<CreativeTabs> {
    @Override
    public JsonElement serialize(CreativeTabs src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject tab = new JsonObject();
        tab.addProperty("index", src.getTabIndex());
        tab.addProperty("label", src.getTabLabel());
        tab.addProperty("translatedLabel", src.getTranslatedTabLabel());
        tab.addProperty("hasSearchBar", src.hasSearchBar());
        tab.addProperty("itemIcon", Item.getIdFromItem(src.getIconItemStack().getItem()));
        return tab;
    }
}
