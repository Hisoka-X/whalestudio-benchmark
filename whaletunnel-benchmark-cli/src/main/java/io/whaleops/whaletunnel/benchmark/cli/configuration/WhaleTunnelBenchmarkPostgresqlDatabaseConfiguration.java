package io.whaleops.whaletunnel.benchmark.cli.configuration;

public class WhaleTunnelBenchmarkPostgresqlDatabaseConfiguration {


    private final String jdbcUrl;
    private final String userName;
    private final String password;

    private static final WhaleTunnelBenchmarkPostgresqlDatabaseConfiguration INSTANCE = new WhaleTunnelBenchmarkPostgresqlDatabaseConfiguration();

    private WhaleTunnelBenchmarkPostgresqlDatabaseConfiguration() {
        WhaleTunnelBenchmarkConfiguration whaleTunnelBenchmarkConfiguration = new WhaleTunnelBenchmarkConfiguration(WhaleTunnelBenchmarkConfiguration.MYSQL_ENV_FILE_PATH);
        this.jdbcUrl = whaleTunnelBenchmarkConfiguration.getProperty("pg.jdbcUrl");
        this.userName = whaleTunnelBenchmarkConfiguration.getProperty("pg.username");
        this.password = whaleTunnelBenchmarkConfiguration.getProperty("pg.password");
    }

    public static WhaleTunnelBenchmarkPostgresqlDatabaseConfiguration getInstance() {
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
