package io.whaleops.whaletunnel.benchmark.cli.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhaleTunnelBenchmarkConfiguration {

    public static final String USER_HOME = System.getProperty("user.home");

    public static final String WHALETUNNEL_BENCHMARK_ENV_HOME = Paths.get(USER_HOME, ".whaletunnel-benchmark").toString();

    public static final String ENV_FILE_PATH = Paths.get(WHALETUNNEL_BENCHMARK_ENV_HOME, "whaletunnel-benchmark.env").toString();
    public static final String MYSQL_ENV_FILE_PATH = Paths.get(WHALETUNNEL_BENCHMARK_ENV_HOME, "whaletunnel-benchmark-database.env").toString();

    public static final Properties properties = new Properties();

    public WhaleTunnelBenchmarkConfiguration(String propertyFilePath) {
        try (FileInputStream fileInputStream = new FileInputStream(propertyFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            log.error("Cannot find the " + ENV_FILE_PATH + " file", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
