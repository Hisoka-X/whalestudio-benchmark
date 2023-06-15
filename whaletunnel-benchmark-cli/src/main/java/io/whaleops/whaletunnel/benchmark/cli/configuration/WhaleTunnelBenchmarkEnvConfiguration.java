package io.whaleops.whaletunnel.benchmark.cli.configuration;

public class WhaleTunnelBenchmarkEnvConfiguration {

    private final String whaleSchedulerHost;
    private final String whaleSchedulerToken;

    private static WhaleTunnelBenchmarkEnvConfiguration INSTANCE = new WhaleTunnelBenchmarkEnvConfiguration();

    private WhaleTunnelBenchmarkEnvConfiguration() {
        WhaleTunnelBenchmarkConfiguration whaleTunnelBenchmarkConfiguration = new WhaleTunnelBenchmarkConfiguration(WhaleTunnelBenchmarkConfiguration.ENV_FILE_PATH);
        this.whaleSchedulerHost = whaleTunnelBenchmarkConfiguration.getProperty("WHALESCHEDULER_HOST");
        this.whaleSchedulerToken = whaleTunnelBenchmarkConfiguration.getProperty("WHALESCHEDULER_TOKEN");
    }

    public static WhaleTunnelBenchmarkEnvConfiguration getInstance() {
        return INSTANCE;
    }

    public String getWhaleSchedulerHost() {
        return whaleSchedulerHost;
    }

    public String getWhaleSchedulerToken() {
        return whaleSchedulerToken;
    }
}
