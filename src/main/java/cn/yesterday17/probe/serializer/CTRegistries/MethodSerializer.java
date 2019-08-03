package cn.yesterday17.probe.serializer.CTRegistries;

import com.google.gson.*;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.lang.reflect.Type;

public class MethodSerializer implements JsonSerializer<IJavaMethod> {
    @Override
    public JsonElement serialize(IJavaMethod src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject method = new JsonObject();

            JsonArray parameters = new JsonArray();
            for (ZenType z:src.getParameterTypes()){
                parameters.add(z.getName());
            }
            method.add("parameters", parameters);

        method.addProperty("returned", src.getReturnType().getName());
        return method;
    }
}
