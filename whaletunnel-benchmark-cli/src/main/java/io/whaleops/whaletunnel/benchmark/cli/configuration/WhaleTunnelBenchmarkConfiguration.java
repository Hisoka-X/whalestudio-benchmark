package io.whaleops.whaletunnel.benchmark.cli.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhaleTunnelBenchmarkConfiguration {

    private static final String ENV_FILE_PATH = Paths.get(System.getProperty("user.home"), ".whalescheduler-benchmark", "whalescheduler-benchmark.env").toString();

    public static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream(ENV_FILE_PATH);) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            // todo: throw exception
            log.error("Cannot find the " + ENV_FILE_PATH + " file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
