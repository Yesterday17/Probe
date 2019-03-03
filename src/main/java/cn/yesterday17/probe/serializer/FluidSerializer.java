package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.lang.reflect.Type;

public class FluidSerializer implements JsonSerializer<Fluid> {
    @Override
    public JsonElement serialize(Fluid src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonFluid = new JsonObject();

        try {
            jsonFluid.addProperty("id", FluidRegistry.getRegisteredFluidIDs().get(src));
            jsonFluid.addProperty("name", src.getName());
            jsonFluid.addProperty("unlocalizedName", src.getUnlocalizedName());
            jsonFluid.add("resourceLocation", context.serialize(new ResourceLocation(FluidRegistry.getDefaultFluidName(src))));
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
        } catch (Exception e) {
            Probe.logger.error("Failed serializing Fluids!");
            Probe.logger.error(e, e);
        }

        return jsonFluid;
    }
}
