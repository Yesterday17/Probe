package cn.yesterday17.probe;

import com.google.gson.annotations.SerializedName;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.*;

public class ZSRCFile {
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
