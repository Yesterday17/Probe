package cn.yesterday17.probe;

import com.google.gson.annotations.SerializedName;
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

    static class ItemEntry {
        String domain;
        String path;
        String unlocalizedName;
        String localizedName;
    }
}
