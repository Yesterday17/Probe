package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.ZSRCSerializer;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.util.HashSet;
import java.util.Set;

@JsonAdapter(ZSRCSerializer.class)
public class ZSRCFile {
    String mcVersion;
    String forgeVersion;

    Set<ModMetadata> Mods = new HashSet<>();

    Set<Item> Items = new HashSet<>();

    Set<Enchantment> Enchantments = new HashSet<>();

    Set<EntityEntry> Entities = new HashSet<>();

    Set<Fluid> Fluids = new HashSet<>();

    public String getMcVersion() {
        return mcVersion;
    }

    public String getForgeVersion() {
        return forgeVersion;
    }

    public Set<ModMetadata> getMods() {
        return Mods;
    }

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
}
