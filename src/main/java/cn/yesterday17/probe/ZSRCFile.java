package cn.yesterday17.probe;

import com.google.gson.annotations.SerializedName;

import java.util.*;

public class ZSRCFile {
    @SerializedName("items")
    Set<ItemEntry> Items = new HashSet<>();

    static class ItemEntry {
        String domain;
        String path;
        String unlocalizedName;
        String localizedName;
    }
}
