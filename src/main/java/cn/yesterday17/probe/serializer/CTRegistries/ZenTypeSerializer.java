package cn.yesterday17.probe.serializer.CTRegistries;

import com.google.gson.*;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.ZenNativeMember;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class ZenTypeSerializer implements JsonSerializer<ZenType> {
    @Override
    public JsonElement serialize(ZenType src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject zenType = new JsonObject();
        zenType.addProperty("zsName", src.getName());

        if (src instanceof ZenTypeNative) {
            JsonObject membersJson = new JsonObject();

            // Members
            ((ZenTypeNative) src).getMembers().forEach(getStringZenNativeMemberBiConsumer(context, membersJson, false));

            // Static Members
            ((ZenTypeNative) src).getStaticMembers().forEach(getStringZenNativeMemberBiConsumer(context, membersJson, true));

            zenType.add("members", membersJson);
        }
        return zenType;
    }

    private BiConsumer<String, ZenNativeMember> getStringZenNativeMemberBiConsumer(JsonSerializationContext context, JsonObject members, boolean isStatic) {
        return (name, member) -> {
            JsonObject memberJson = new JsonObject();
            memberJson.addProperty("static", isStatic);

            // Get all callable of the method
            if (!member.getMethods().isEmpty()) {
                JsonArray methods = new JsonArray();
                member.getMethods().forEach(
                        method -> methods.add(context.serialize(method, IJavaMethod.class))
                );
                memberJson.add("methods", methods);
            }

            // Getter object
            if (member.getGetter() != null) {
                memberJson.addProperty("getter", member.getGetter().getReturnType().getName());
            }

            // Setter object
            if (member.getSetter() != null) {
                JsonArray setterTypes = new JsonArray();
                for (ZenType j : member.getSetter().getParameterTypes()) {
                    setterTypes.add(j.getName());
                }
                memberJson.add("setter", setterTypes);
            }

            members.add(name, memberJson);
        };
    }
}
