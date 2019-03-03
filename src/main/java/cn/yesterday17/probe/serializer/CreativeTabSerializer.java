package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
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
        try {
            tab.addProperty("label", src.getTabLabel());
            tab.addProperty("translatedLabel", src.getTranslatedTabLabel());
            tab.addProperty("hasSearchBar", src.hasSearchBar());
            tab.addProperty("itemIcon", Item.getIdFromItem(src.getIconItemStack().getItem()));
        } catch (Exception e) {
            Probe.logger.error("Failed serializing CreativeTabs!");
            Probe.logger.error(e, e);
        }
        return tab;
    }
}
