package test.helper;

public enum DatabaseType {
    AUTH("rococo-auth", "infrastructure-auth-rococo-db-1", 3306),
    GATEWAY("rococo-gateway", "infrastructure-gateway-rococo-db-1", 3307);

    private final String databaseName;
    private final String containerName;
    private final int port;

    DatabaseType(String databaseName, String containerName, int port) {
        this.databaseName = databaseName;
        this.containerName = containerName;
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getContainerName() {
        return containerName;
    }

    public int getPort() {
        return port;
    }

    public String getJdbcUrl() {
        return String.format("jdbc:mysql://localhost:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                port, databaseName);
    }
}