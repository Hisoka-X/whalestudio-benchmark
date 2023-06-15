package io.whaleops.whaletunnel.benchmark.cli.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkConfiguration;
import io.whaleops.whaletunnel.benchmark.cli.exception.CommandException;
import io.whaleops.whaletunnel.benchmark.cli.exception.InitializeEnvCommandExceptionStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class InitializeEnvCommand {

    @ShellMethod("Initialize Env e.g. Create whaletunnel-benchmark.env file")
    public void initializeEnv() throws IOException {
        initializeEnvFile();
    }

    private void initializeEnvFile() throws CommandException {
        try {
            Path envHome = Paths.get(WhaleTunnelBenchmarkConfiguration.WHALETUNNEL_BENCHMARK_ENV_HOME);
            if (Files.exists(envHome)) {
                Files.deleteIfExists(envHome);
            }
            Files.createDirectories(envHome);
            log.info("Success create env home: {}", WhaleTunnelBenchmarkConfiguration.WHALETUNNEL_BENCHMARK_ENV_HOME);
            // todo: can we list the resource file?
            try (InputStream resourceAsStream = InitializeEnvCommand.class.getClassLoader().getResourceAsStream(".whaletunnel-benchmark/whaletunnel-benchmark.env");) {
                Files.copy(resourceAsStream, Paths.get(WhaleTunnelBenchmarkConfiguration.ENV_FILE_PATH));
                log.info("Success create env file: {}", WhaleTunnelBenchmarkConfiguration.ENV_FILE_PATH);
            }
            try (InputStream resourceAsStream = InitializeEnvCommand.class.getClassLoader().getResourceAsStream(".whaletunnel-benchmark/whaletunnel-benchmark-mysql.env");) {
                Files.copy(resourceAsStream, Paths.get(WhaleTunnelBenchmarkConfiguration.MYSQL_ENV_FILE_PATH));
                log.info("Success create env file: {}", WhaleTunnelBenchmarkConfiguration.MYSQL_ENV_FILE_PATH);
            }
        } catch (Exception ex) {
            throw CommandException.newCommandException(InitializeEnvCommandExceptionStatus.INITIALIZE_ENV_FILE_EXCEPTION_STATUS, ex);
        }
    }
}
