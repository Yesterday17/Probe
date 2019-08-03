package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.ZSRCSerializer;
import com.google.gson.annotations.JsonAdapter;
import mezz.jei.gui.ingredients.IIngredientListElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.dump.types.DumpZenType;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonAdapter(ZSRCSerializer.class)
public class ZSRCFile {
    String mcVersion;
    String forgeVersion;
    String probeVersion;

    Set<ModContainer> Mods = new HashSet<>();

    List<IIngredientListElement> JEIItems = new ArrayList<>();

    @Deprecated
    Set<Item> Items = new HashSet<>();

    Set<Enchantment> Enchantments = new HashSet<>();

    Set<EntityEntry> Entities = new HashSet<>();

    Set<Fluid> Fluids = new HashSet<>();

    Set<String> OreDictionary = new HashSet<>();

    List<ZenType> zenTypes = new ArrayList<>();

    public String getMcVersion() {
        return mcVersion;
    }

    public String getForgeVersion() {
        return forgeVersion;
    }

    public String getProbeVersion() {
        return probeVersion;
    }

    public Set<ModContainer> getMods() {
        return Mods;
    }

    public List<IIngredientListElement> getJEIItems() {
        return JEIItems;
    }

    @Deprecated
    public Set<Item> getItems() {
        return Items;
    }

    public Set<Enchantment> getEnchantments() {
        return Enchantments;
    }

    public Set<EntityEntry> getEntities() {
        return Entities;
    }

    public Set<Fluid> getFluids() {
        return Fluids;
    }

    public Set<String> getOreDictionary() {
        return OreDictionary;
    }

    public List<ZenType> getZenTypes() {
        return zenTypes;
    }
}
