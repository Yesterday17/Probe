package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.ZSRCSerializer;
import com.google.gson.annotations.JsonAdapter;
import mezz.jei.gui.ingredients.IIngredientListElement;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.*;

@JsonAdapter(ZSRCSerializer.class)
public class ZSRCFile {
    String mcVersion;
    String forgeVersion;
    String probeVersion;

    Set<ModContainer> Mods = new HashSet<>();

    List<IIngredientListElement> JEIItems = new ArrayList<>();

    Set<Enchantment> Enchantments = new HashSet<>();

    Set<EntityEntry> Entities = new HashSet<>();

    Set<Fluid> Fluids = new HashSet<>();

    Set<String> OreDictionary = new HashSet<>();

    Set<String> ZenType = new HashSet<>();

    Map<String, ZenType> ZenPackages = new HashMap<>();

    Map<String, String> GlobalFields = new HashMap<>();

    Map<String, IJavaMethod> GlobalMethods = new HashMap<>();

    Map<String, IJavaMethod> GlobalGetters = new HashMap<>();

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

    public Set<String> getZenType() {
        return ZenType;
    }

    public Map<String, ZenType> getZenPackages() {
        return ZenPackages;
    }

    public Map<String, String> getGlobalFields() {
        return GlobalFields;
    }

    public Map<String, IJavaMethod> getGlobalMethods() {
        return GlobalMethods;
    }

    public Map<String, IJavaMethod> getGlobalGetters() {
        return GlobalGetters;
    }
}
