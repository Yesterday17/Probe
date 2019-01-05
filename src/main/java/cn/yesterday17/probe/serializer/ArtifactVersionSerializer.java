package cn.yesterday17.probe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import java.lang.reflect.Type;

public class ArtifactVersionSerializer implements JsonSerializer<ArtifactVersion> {
    @Override
    public JsonElement serialize(ArtifactVersion src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject artifactVersion = new JsonObject();
        artifactVersion.addProperty("label", src.getLabel());
        artifactVersion.addProperty("version", src.getVersionString());
        artifactVersion.addProperty("range", src.getRangeString());
        return artifactVersion;
    }
}
