package cn.yesterday17.probe;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.HashSet;
import java.util.Set;

public class ZSRCFile {
    String mcVersion;
    String forgeVersion;

    @SerializedName("mods")
    Set<ModMetadata> Mods = new HashSet<>();

    @SerializedName("items")
    Set<ItemEntry> Items = new HashSet<>();

    @SerializedName("enchantments")
    Set<EnchantmentEntry> Enchantments = new HashSet<>();

    @SerializedName("entities")
    Set<EntityEntry> Entities = new HashSet<>();

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
