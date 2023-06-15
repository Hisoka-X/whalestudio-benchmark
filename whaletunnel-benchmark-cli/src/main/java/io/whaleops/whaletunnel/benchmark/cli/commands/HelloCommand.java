package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HelloCommand {

    @ShellMethod(key = "hello")
    public String sayHello(@ShellOption(defaultValue = "whaletunnel-benchmark-cli") String name) {
        return "Hello world " + name + "!";
    }
}
