package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HelloCommand {

    @ShellMethod(key = "hello", value = "Say hello. e.g. hello --n wenjun")
    public String sayHello(@ShellOption(defaultValue = "whaletunnel-benchmark-cli", value = "n") String name) {
        return "Hello world " + name + "!";
    }
}
