package eu.ovecode.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.ovecode.OveCode;
import eu.ovecode.manager.LoggerManager;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@SuppressWarnings("all")
public class ConfigManager {

    private final File file;
    @Getter
    private Config config;

    public ConfigManager(OveCode oveCode) {
        this.file = new File(oveCode.getJarPath() + "/config.json");
        load();
    }

    public void load() {
        if (file.exists()) {
            try {
                JSONObject jsonObject = new JSONObject(Files.readString(file.toPath(), StandardCharsets.UTF_8));
                parseConfig(jsonObject);
            } catch (JSONException jsonException) {
                LoggerManager.error("Błąd w odczycie jsona configu", jsonException);
            } catch (IOException ioException) {
                LoggerManager.error("Błąd w odczycie byteów configu", ioException);
            }
        } else {
            LoggerManager.warn("Brak pliku konfiguracyjnego bota... Kopiuję nowy.");
            try {
                URL resourceUrl = getClass().getResource("/config.json");
                Files.copy(Paths.get(resourceUrl.toURI()), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                load();
            } catch (Exception exception) {
                LoggerManager.error("Błąd w powieleniu domyślnego pliku konfiguracyjnego", exception);
            }
        }
    }

    @SneakyThrows
    private void parseConfig(JSONObject jsonObject) {
        ObjectMapper m = new ObjectMapper();
        this.config = m.readValue(jsonObject.toString(), Config.class);
    }

}
