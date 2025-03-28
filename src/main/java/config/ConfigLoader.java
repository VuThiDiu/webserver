package config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;

public class ConfigLoader {


    public ConfigLoader() {
    }

    public ApplicationConfig loadConfig(String fileName) {
        Logger logger = LogManager.getLogger(ConfigLoader.class);
        File file = new File(fileName);
        if (!file.exists()) logger.error("File not found: " + fileName);

        Yaml yaml = new Yaml();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return yaml.loadAs(fileInputStream, ApplicationConfig.class);
        } catch (Exception e) {
            logger.error("Error loading config: " + e.getMessage());
        }
        return new ApplicationConfig();
    }


}
