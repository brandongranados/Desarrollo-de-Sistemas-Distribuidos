import java.lang.reflect.Type;
import com.google.gson.*;

public class formatDateTimeGSON implements JsonDeserializer<String>, JsonSerializer<String>
{
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String fecha = json.getAsJsonPrimitive().getAsString();
        if(fecha.length() == 24 && fecha.charAt(23)=='Z' && fecha.charAt(10) == 'T')
            return fecha.replace("T", " ").replace("Z", "");
        else
            return fecha;
    }
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        if(src.length() == 23 && src.charAt(10) == ' ')
            return new JsonPrimitive(src.replace(" ", "T").concat("Z"));
        else
            return new JsonPrimitive(src);
    }
}