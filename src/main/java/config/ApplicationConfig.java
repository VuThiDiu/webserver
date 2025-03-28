package config;


import lombok.Data;

@Data
public class ApplicationConfig {
    private int port;
    private int threads;
    private int sessionTimeout;

    /* Create singleton applicationConfig*/
    private static volatile ApplicationConfig applicationConfig;

    public static ApplicationConfig getInstance() {
        if (applicationConfig == null) {
            synchronized (ApplicationConfig.class) {
                if (applicationConfig == null) {
                    ConfigLoader configLoader = new ConfigLoader();
                    applicationConfig = configLoader.loadConfig("config/application.yml");
                }
            }
        }
        return applicationConfig;
    }
}
