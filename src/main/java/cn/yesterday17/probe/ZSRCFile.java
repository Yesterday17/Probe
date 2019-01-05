package cn.yesterday17.probe;

import cn.yesterday17.probe.serializer.ZSRCSerializer;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.HashSet;
import java.util.Set;

@JsonAdapter(ZSRCSerializer.class)
public class ZSRCFile {
    String mcVersion;
    String forgeVersion;

    Set<ModMetadata> Mods = new HashSet<>();

    Set<ItemEntry> Items = new HashSet<>();

    Set<EnchantmentEntry> Enchantments = new HashSet<>();

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

    public Set<ItemEntry> getItems() {
        return Items;
    }

    public Set<EnchantmentEntry> getEnchantments() {
        return Enchantments;
    }

    public Set<EntityEntry> getEntities() {
        return Entities;
    }

    public Set<Fluid> getFluids() {
        return Fluids;
    }

    static class BaseEntry {
        String domain;
        String path;
        String unlocalizedName;
        String localizedName;

        BaseEntry(ResourceLocation resourceLocation) {
            this.domain = resourceLocation.getResourceDomain();
            this.path = resourceLocation.getResourcePath();
        }

        void setUnlocalizedName(String unlocalizedName, String pre, String post) {
            this.unlocalizedName = unlocalizedName;
            this.localizedName = I18n.format(pre + this.unlocalizedName + post);
        }

        void setUnlocalizedName(String unlocalizedName, String post) {
            this.setUnlocalizedName(unlocalizedName, "", post);
        }

        void setUnlocalizedName(String unlocalizedName) {
            this.setUnlocalizedName(unlocalizedName, "", "");
        }

    }

    static class ItemEntry extends BaseEntry{
        ItemEntry(ResourceLocation resourceLocation) {
            super(resourceLocation);
        }
    }

    static class EnchantmentEntry extends BaseEntry{
        String rarity;
        String type;

        EnchantmentEntry(ResourceLocation resourceLocation) {
            super(resourceLocation);
        }
    }

    static class EntityEntry extends BaseEntry {
        private transient String unlocalizedName;

        EntityEntry(ResourceLocation resourceLocation){
            super(resourceLocation);
        }

        @Override
        void setUnlocalizedName(String unlocalizedName, String pre, String post) {
            this.unlocalizedName = unlocalizedName;
            this.localizedName = unlocalizedName;
        }
    }
}
