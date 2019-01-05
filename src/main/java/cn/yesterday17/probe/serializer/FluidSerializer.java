package cn.yesterday17.probe.serializer;

import com.google.gson.*;
import net.minecraftforge.fluids.Fluid;

import java.lang.reflect.Type;

public class FluidSerializer implements JsonSerializer<Fluid> {
    @Override
    public JsonElement serialize(Fluid src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonFluid = new JsonObject();
        jsonFluid.addProperty("name", src.getName());
        jsonFluid.addProperty("unlocalizedName", src.getUnlocalizedName());
        jsonFluid.addProperty("luminosity", src.getLuminosity());
        jsonFluid.addProperty("density", src.getDensity());
        jsonFluid.addProperty("temperature", src.getTemperature());
        jsonFluid.addProperty("viscosity", src.getViscosity());
        jsonFluid.addProperty("isGaseous", src.isGaseous());
        jsonFluid.addProperty("rarity", src.getRarity().toString());
        jsonFluid.addProperty("color", src.getColor());
        // jsonFluid.add("block", context.serialize(src.getBlock()));
        jsonFluid.add("still", context.serialize(src.getStill()));
        jsonFluid.add("flowing", context.serialize(src.getFlowing()));
        return jsonFluid;
    }
}
