package io.whaleops.whaletunnel.benchmark.cli.configuration;

public class WhaleTunnelBenchmarkMysqlDatabaseConfiguration {

    private final String jdbcUrl;
    private final String userName;
    private final String password;

    private static final WhaleTunnelBenchmarkMysqlDatabaseConfiguration INSTANCE = new WhaleTunnelBenchmarkMysqlDatabaseConfiguration();

    private WhaleTunnelBenchmarkMysqlDatabaseConfiguration() {
        WhaleTunnelBenchmarkConfiguration whaleTunnelBenchmarkConfiguration = new WhaleTunnelBenchmarkConfiguration(WhaleTunnelBenchmarkConfiguration.MYSQL_ENV_FILE_PATH);
        this.jdbcUrl = whaleTunnelBenchmarkConfiguration.getProperty("jdbcUrl");
        this.userName = whaleTunnelBenchmarkConfiguration.getProperty("username");
        this.password = whaleTunnelBenchmarkConfiguration.getProperty("password");
    }

    public static WhaleTunnelBenchmarkMysqlDatabaseConfiguration getInstance() {
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
