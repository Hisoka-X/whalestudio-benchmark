package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import io.whaleops.whaletunnel.benchmark.cli.model.project.Project;
import io.whaleops.whaletunnel.benchmark.cli.sdk.WhaleSchedulerSdk;
import io.whaleops.whaletunnel.benchmark.cli.utils.ConsoleUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class ProjectCommand {

    @ShellMethod(value = "List all projects", key = "lp")
    public List<Project> listProjects() {
        List<Project> projects = WhaleSchedulerSdk.listProjects();
        if (CollectionUtils.isEmpty(projects)) {
            return projects;
        }
        StringBuilder stringBuilder = new StringBuilder();
        // todo: use table format
        stringBuilder.append("ProjectName").append("\t").append("ProjectCode").append("\n");
        for (Project project : projects) {
            stringBuilder.append(project.getName()).append("\t").append(project.getCode()).append("\n");
        }
        ConsoleUtils.console(stringBuilder.toString());
        return projects;
    }

}
