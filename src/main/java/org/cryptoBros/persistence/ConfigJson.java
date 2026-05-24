package org.cryptoBros.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cryptoBros.persistence.Deserializers.ConfigDeserializer;
import org.cryptoBros.persistence.Exceptions.ConfigFileCorruptedException;
import org.cryptoBros.persistence.Exceptions.ConfigFileNotFoundException;

import java.io.FileReader;
import java.io.IOException;

/**
 * JSON-based implementation of ConfigPersistence.
 */
public class ConfigJson implements ConfigPersistence {
    private final Gson gson;
    private static final String CONFIG_FILEPATH = "src/main/java/org/cryptoBros/config.json";

    /**
     * Initializes Gson with a custom Config deserializer.
     */
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

    /**
     * Reads the admin password from the JSON configuration file.
     *
     * @return the admin password string
     * @throws ConfigFileNotFoundException if the config file cannot be found or read
     */
    @Override
    public String readAdminPassword() throws ConfigFileNotFoundException {
        return readConfig().adminPassword();
    }

    /**
     * Reads database connection credentials from the JSON configuration file and validates required fields.
     *
     * @return the database credentials containing port, IP, database name, username, and password
     * @throws ConfigFileNotFoundException  if the config file cannot be found or read
     * @throws ConfigFileCorruptedException if the config file is missing required fields
     */
    @Override
    public DbCredentials readCredentials() throws ConfigFileNotFoundException, ConfigFileCorruptedException {
        Config c = readConfig();
        if (readConfig() == null) throw new ConfigFileNotFoundException("Failed to read config");
        if (c.port() == -1 || c.ip() == null || c.dbName() == null || c.username() == null || c.password() == null) throw new ConfigFileCorruptedException("Some data in the config file is missing");
        return new DbCredentials(c.port(), c.ip(), c.dbName(), c.username(), c.password());
    }
}
