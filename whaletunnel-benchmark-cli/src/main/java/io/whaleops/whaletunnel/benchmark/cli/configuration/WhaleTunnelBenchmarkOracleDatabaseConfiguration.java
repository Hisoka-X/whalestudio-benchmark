package io.whaleops.whaletunnel.benchmark.cli.configuration;

public class WhaleTunnelBenchmarkOracleDatabaseConfiguration {

    private final String jdbcUrl;
    private final String userName;
    private final String password;

    private static final WhaleTunnelBenchmarkOracleDatabaseConfiguration INSTANCE = new WhaleTunnelBenchmarkOracleDatabaseConfiguration();

    private WhaleTunnelBenchmarkOracleDatabaseConfiguration() {
        WhaleTunnelBenchmarkConfiguration whaleTunnelBenchmarkConfiguration = new WhaleTunnelBenchmarkConfiguration(WhaleTunnelBenchmarkConfiguration.MYSQL_ENV_FILE_PATH);
        this.jdbcUrl = whaleTunnelBenchmarkConfiguration.getProperty("oracle.jdbcUrl");
        this.userName = whaleTunnelBenchmarkConfiguration.getProperty("oracle.username");
        this.password = whaleTunnelBenchmarkConfiguration.getProperty("oracle.password");
    }

    public static WhaleTunnelBenchmarkOracleDatabaseConfiguration getInstance() {
        return INSTANCE;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
