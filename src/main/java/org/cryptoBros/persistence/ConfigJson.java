package org.cryptoBros.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cryptoBros.persistence.Deserializers.ConfigDeserializer;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

import java.io.FileReader;
import java.io.IOException;

public class ConfigJson implements ConfigPersistence {
    private final Gson gson;
    private static final String CONFIG_FILEPATH = "src/main/java/org/cryptoBros/config.json";

    public ConfigJson() {
        gson = new GsonBuilder().registerTypeAdapter(Config.class, new ConfigDeserializer()).create();
    }

    private Config readConfig() throws ConfigFileNotFoundException {
        try (FileReader reader = new FileReader(CONFIG_FILEPATH)) {
            return gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConfigFileNotFoundException("Failed to read config file");
        }
    }

    @Override
    public String readAdminPassword() throws ConfigFileNotFoundException {
        return readConfig().adminPassword();
    }

    @Override
    public DbCredentials readCredentials() throws ConfigFileNotFoundException {
        Config c = readConfig();
        return new DbCredentials(c.port(), c.ip(), c.dbName(), c.username(), c.password());
    }
}
