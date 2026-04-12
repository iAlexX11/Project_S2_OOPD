package org.cryptoBros.persistence.Deserializers;

import com.google.gson.*;
import org.cryptoBros.persistence.Config;

import java.lang.reflect.Type;

public class ConfigDeserializer implements JsonDeserializer<Config> {
    @Override
    public Config deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int port = jsonObject.get("port").getAsInt();
        String ip = jsonObject.get("ip").getAsString();
        String database = jsonObject.get("database").getAsString();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String adminPassword = jsonObject.get("admin_password").getAsString();
        return new Config(port, ip, database, username, password, adminPassword);
    }
}
