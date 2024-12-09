package http;

import com.google.gson.*;
import model.Epic;

import java.lang.reflect.Type;

public class EpicDeserializer implements JsonDeserializer<Epic> {

    @Override
    public Epic deserialize(JsonElement jsonElement,
                            Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name") != null ? jsonObject.get("name").getAsString() : null;
        String description = jsonObject.get("description") != null ? jsonObject.get("description").getAsString() : null;
        int id = jsonObject.get("id").getAsInt();
        Epic epic = new Epic(name, description);
        epic.setId(id);
        return epic;
    }

}
