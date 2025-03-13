package utils;

public class LoggingSystem {
    public static void buildLogs(String... args) {
        StringBuilder log = new StringBuilder();
        int i = 0;
        for (String arg : args) {
            if (i++ % 2 == 0) log.append("\n");
            log.append(arg).append(" ");
        }
        System.out.println(log);
    }

}
