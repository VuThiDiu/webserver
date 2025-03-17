package config;

import service.ConfigLoader;


public class ApplicationConfig {
    private int port;
    private int threads;

    public int getPort(){
        return port;
    }

    public int getThreads(){
        return threads;
    }
    public void setThreads(int threads){
        this.threads = threads;
    }
    public void setPort(int port){
        this.port = port;
    }

    /* Create singleton applicationConfig*/
    private static ApplicationConfig applicationConfig;
    public static synchronized ApplicationConfig getInstance(){
        if(applicationConfig == null){
            ConfigLoader configLoader = ConfigLoader.getInstance();
            applicationConfig = configLoader.loadConfig("config/application.yml");
        }
        return applicationConfig;
    }
}
