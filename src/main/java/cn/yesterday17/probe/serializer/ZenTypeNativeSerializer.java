package cn.yesterday17.probe.serializer;

import com.google.gson.*;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.ZenNativeMember;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class ZenTypeNativeSerializer implements JsonSerializer<ZenTypeNative> {

    @Override
    public JsonElement serialize(ZenTypeNative src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject zenTypeNative = new JsonObject();
        // Members
        JsonObject membersJson = new JsonObject();
        src.getMembers().forEach(getStringZenNativeMemberBiConsumer(context, membersJson));
        zenTypeNative.add("members", membersJson);

        // Static Members
        membersJson = new JsonObject();
        src.getStaticMembers().forEach(getStringZenNativeMemberBiConsumer(context, membersJson));
        zenTypeNative.add("staticMembers", membersJson);

        return zenTypeNative;
    }

    private BiConsumer<String, ZenNativeMember> getStringZenNativeMemberBiConsumer(JsonSerializationContext context, JsonObject members) {
        return (name, member) -> {
            JsonObject memberJson = new JsonObject();

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
