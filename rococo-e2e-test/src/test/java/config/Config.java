package config;

public interface Config {
    String gatewayUrl();
    String authUrl();
    String frontUrl();
    String userdataUrl();
    String spendUrl();
    String githubUrl();
    String spendJdbcUrl();
    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }
}