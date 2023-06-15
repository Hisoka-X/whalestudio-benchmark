package io.whaleops.whaletunnel.benchmark.cli.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkConfiguration;
import io.whaleops.whaletunnel.benchmark.cli.exception.CommandException;
import io.whaleops.whaletunnel.benchmark.cli.exception.InitializeEnvCommandExceptionStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class InitializeEnvCommand {

    @ShellMethod("Initialize Env e.g. Create whalescheduler-benchmark.env file")
    public void initializeEnv() throws IOException {
        initializeEnvFile();
    }

    private void initializeEnvFile() throws CommandException {
        try {
            Path envDirPath = Paths.get(WhaleTunnelBenchmarkConfiguration.ENV_FILE_PATH).getParent();
            if (Files.exists(envDirPath)) {
                Files.deleteIfExists(envDirPath);
            }
            Files.createDirectories(envDirPath);
            try (InputStream resourceAsStream = InitializeEnvCommand.class.getClassLoader().getResourceAsStream(".whalescheduler-benchmark/whalescheduler-benchmark.env");) {
                Files.copy(resourceAsStream, Paths.get(WhaleTunnelBenchmarkConfiguration.ENV_FILE_PATH));
            }
        } catch (Exception ex) {
            throw CommandException.newCommandException(InitializeEnvCommandExceptionStatus.INITIALIZE_ENV_FILE_EXCEPTION_STATUS, ex);
        }
    }
}
