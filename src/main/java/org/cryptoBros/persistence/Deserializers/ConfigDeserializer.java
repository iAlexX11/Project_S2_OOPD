package org.cryptoBros.persistence.Deserializers;

import com.google.gson.*;
import org.cryptoBros.persistence.Config;

import java.lang.reflect.Type;

public class ConfigDeserializer implements JsonDeserializer<Config> {
    @Override
    public Config deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int port = getInt(jsonObject, "port");
        String ip = getString(jsonObject, "ip");
        String database = getString(jsonObject, "database");
        String username = getString(jsonObject, "username");
        String password = getString(jsonObject, "password");
        String adminPassword = getString(jsonObject, "admin_password");
        return new Config(port, ip, database, username, password, adminPassword);
    }



    private String getString(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        return el != null ? el.getAsString() : null;
    }

    private int getInt(JsonObject obj, String key) {
        JsonElement el = obj.get(key);
        return el != null ? el.getAsInt() : -1;
    }
}