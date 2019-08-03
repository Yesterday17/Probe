package cn.yesterday17.probe;

import net.minecraftforge.common.config.Config;

@Config(modid = Probe.MOD_ID, name = Probe.NAME)
public class ProbeConfig {
    @Config.RequiresMcRestart
    public static boolean enableMods = true;

    @Config.RequiresMcRestart
    public static boolean enableItems = true;

    @Config.RequiresMcRestart
    public static boolean enableEnchantments = true;

    @Config.RequiresMcRestart
    public static boolean enableEntities = true;

    @Config.RequiresMcRestart
    public static boolean enableFluids = true;

    @Config.RequiresMcRestart
    public static boolean enableOreDictionary = true;

    @Config.RequiresMcRestart
    public static boolean enableRegistries = true;

    @Config.RequiresMcRestart
    public static boolean setPrettyPrinting = false;

}
