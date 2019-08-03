package cn.yesterday17.probe.serializer.CTRegistries;

import com.google.gson.*;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.ZenTypeNative;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.lang.reflect.Type;

public class ZenTypeSerializer implements JsonSerializer<ZenType> {

    @Override
    public JsonElement serialize(ZenType src, Type typeOfSrc, JsonSerializationContext context) {

        //Base ZenType
        JsonObject zentype = new JsonObject();
        zentype.addProperty("zsName", src.getName());


        if (src instanceof ZenTypeNative){
            //Members JsonObject
            JsonObject membersJson = new JsonObject();

            ((ZenTypeNative) src).getMembers().forEach((strname, member)-> {
                JsonObject memberJson = new JsonObject();

                //Get all callable of the method
                if (!member.getMethods().isEmpty()){
                    JsonArray methods = new JsonArray();
                    member.getMethods().forEach((
                            iJavaMethod ->
                            methods.add(context.serialize(iJavaMethod, IJavaMethod.class))));
                    memberJson.add("methods", methods);
                }


                //Getter object
                if (member.getGetter() != null){
                    memberJson.addProperty("getter", member.getGetter().getReturnType().getName());
                }

                //Setter object
                if (member.getSetter() != null){
                    JsonArray setterTypes = new JsonArray();
                    for (ZenType j : member.getSetter().getParameterTypes()){
                        setterTypes.add(j.getName());
                    }
                    memberJson.add("setter", setterTypes);
                }
                membersJson.add(strname, memberJson);
            });

            //Static
            ((ZenTypeNative) src).getStaticMembers().forEach((strname, member)-> {
                JsonObject memberJson = new JsonObject();

                //Get all callable of the method
                if (!member.getMethods().isEmpty()){
                    JsonArray methods = new JsonArray();
                    member.getMethods().forEach((
                            iJavaMethod ->
                                    methods.add(context.serialize(iJavaMethod, IJavaMethod.class))));
                    memberJson.add("methods", methods);
                }


                //Getter object
                if (member.getGetter() != null){
                    memberJson.addProperty("getter", member.getGetter().getReturnType().getName());
                }

                //Setter object
                if (member.getSetter() != null){
                    JsonArray setterTypes = new JsonArray();
                    for (ZenType j : member.getSetter().getParameterTypes()){
                        setterTypes.add(j.getName());
                    }
                    memberJson.add("setter", setterTypes);
                }
                membersJson.add(strname, memberJson);
            });

            zentype.add("members", membersJson);
        }
        return zentype;
    }
}
