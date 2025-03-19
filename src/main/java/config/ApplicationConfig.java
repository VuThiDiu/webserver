package config;


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
    private static volatile ApplicationConfig applicationConfig;
    public static ApplicationConfig getInstance(){
        if(applicationConfig == null){
            synchronized (ApplicationConfig.class){
                if(applicationConfig == null){
                    ConfigLoader configLoader = new ConfigLoader();
                    applicationConfig = configLoader.loadConfig("config/application.yml");
                }
            }
        }
        return applicationConfig;
    }
}
