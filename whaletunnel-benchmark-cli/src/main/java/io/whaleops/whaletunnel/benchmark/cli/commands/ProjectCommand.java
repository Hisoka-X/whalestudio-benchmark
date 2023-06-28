package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import com.google.common.collect.Lists;

import io.whaleops.whaletunnel.benchmark.cli.model.RowTable;
import io.whaleops.whaletunnel.benchmark.cli.model.project.Project;
import io.whaleops.whaletunnel.benchmark.cli.sdk.WhaleSchedulerSdk;
import io.whaleops.whaletunnel.benchmark.cli.utils.TableFormatUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class ProjectCommand {

    @ShellMethod(value = "List all projects, e.g. lp", key = "lp")
    public String listProjects() {
        List<Project> projects = WhaleSchedulerSdk.listProjects();
        if (CollectionUtils.isEmpty(projects)) {
            return "";
        }
        List<String> tableHeaders = Lists.newArrayList("ProjectName", "ProjectCode");
        List<List<String>> tableBody = new ArrayList<>();
        for (Project project : projects) {
            tableBody.add(Lists.newArrayList(project.getName(), String.valueOf(project.getCode())));
        }
        RowTable rowTable = new RowTable(tableHeaders, tableBody);
        return TableFormatUtils.formatTable(rowTable);
    }

}
