package config;

public class Config {

    private static Config instance;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String gatewayUrl() {
        return System.getProperty("gateway.url", "http://localhost:9001");
    }

    public String authUrl() {
        return System.getProperty("auth.url", "http://localhost:9000");
    }

    public String frontUrl() {
        return System.getProperty("front.url", "http://localhost:3000");
    }

    public String userdataUrl() {
        return System.getProperty("userdata.url", "http://localhost:9002");
    }
    public String spendUrl() {
        return System.getProperty("userdata.url", "http://localhost:8080");
    }
}
