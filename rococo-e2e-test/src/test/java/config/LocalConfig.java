package config;


enum LocalConfig implements Config {
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://localhost:9000";
    }

    @Override
    public String spendUrl() {
        return "http://localhost:8080/";
    }

    @Override
    public String spendJdbcUrl() {
        return "jdbc:mysql://localhost:3306/rococo-gateway";
    }

    @Override
    public String githubUrl() {
        return "https://api.github.com/";
    }
}
