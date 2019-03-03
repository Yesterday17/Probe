package cn.yesterday17.probe.serializer;

import cn.yesterday17.probe.Probe;
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
        try {
            artifactVersion.addProperty("label", src.getLabel());
            artifactVersion.addProperty("version", src.getVersionString());
            artifactVersion.addProperty("range", src.getRangeString());
        } catch (Exception e) {
            Probe.logger.error("Failed serializing ArtifactVersion!");
            Probe.logger.error(e, e);
        }
        return artifactVersion;
    }
}
