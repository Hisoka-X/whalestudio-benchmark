package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.google.common.collect.Lists;

import io.whaleops.whaletunnel.benchmark.cli.model.RowTable;
import io.whaleops.whaletunnel.benchmark.cli.model.workflowinstance.WorkflowInstance;
import io.whaleops.whaletunnel.benchmark.cli.sdk.WhaleSchedulerSdk;
import io.whaleops.whaletunnel.benchmark.cli.utils.TableFormatUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class WorkflowInstanceCommand {

    @ShellMethod(key = "kill-wfi", value = "Kill workflow instance under given projects, e.g. kill-wfi -wfi 1,2,3")
    public void killWorkflowInstance(@ShellOption(value = "p", help = "Project codes") List<Long> projectCodes) {
        List<WorkflowInstance> workflowInstances = WhaleSchedulerSdk.listWorkflowInstances(projectCodes);
        if (CollectionUtils.isEmpty(workflowInstances)) {
            log.warn("There is no workflow instance under given projects: {}", projectCodes);
            return;
        }
        for (WorkflowInstance workflowInstance : workflowInstances) {
            WhaleSchedulerSdk.killWorkflowInstance(workflowInstance.getId());
            log.info("Success kill workflow instance: {} under given project", workflowInstance.getName());
        }
    }

    @ShellMethod(key = "restart-wfi", value = "Restart workflow instance under given projects, e.g. restart-wfi -p 1,2,3")
    public void restartWorkflowInstance(@ShellOption(value = "p", help = "Project codes") List<Long> projectCodes) {
        List<WorkflowInstance> workflowInstances = WhaleSchedulerSdk.listWorkflowInstances(projectCodes);
        if (CollectionUtils.isEmpty(workflowInstances)) {
            log.warn("There is no workflow instance under given projects: {}", projectCodes);
            return;
        }
        for (WorkflowInstance workflowInstance : workflowInstances) {
            // todo: check the workflow instance is finished
            WhaleSchedulerSdk.restartWorkflowInstance(workflowInstance.getId());
            log.info("Success kill workflow instance: {} under given project", workflowInstance.getName());
        }
    }

    @ShellMethod(key = "delete-wfi", value = "Clear workflow instance under given projects, e.g. delete-wfi -p 1,2,3")
    public void clearWorkflowInstance(@ShellOption(value = "p", help = "Project codes") List<Long> projectCodes) {
        List<WorkflowInstance> workflowInstances = WhaleSchedulerSdk.listWorkflowInstances(projectCodes);
        if (CollectionUtils.isEmpty(workflowInstances)) {
            log.warn("There is no workflow instance under given projects: {}", projectCodes);
            return;
        }
        Lists.partition(workflowInstances, 20)
            .forEach(subWorkflowInstances -> {
                String workflowInstanceIds = subWorkflowInstances.stream()
                    .map(WorkflowInstance::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
                WhaleSchedulerSdk.deleteWorkflowInstance(workflowInstanceIds);
                log.info("Success delete workflow instances: {} under given project", subWorkflowInstances.size());
            });
    }

    @ShellMethod(key = "analysis-wfi", value = "Query workflow instance status under given projects, e.g. analysis-wfi -p 1,2,3")
    public String analysisWorkflowInstance(@ShellOption(value = "p", help = "projectCodes") List<Long> projectCodes) {
        List<WorkflowInstance> workflowInstances = WhaleSchedulerSdk.listWorkflowInstances(projectCodes);
        if (CollectionUtils.isEmpty(workflowInstances)) {
            log.warn("There is no workflow instance under given projects: {}", projectCodes);
            return "";
        }

        Map<String, Long> workflowInstanceMap = new HashMap<>();
        for (WorkflowInstance workflowInstance : workflowInstances) {
            workflowInstanceMap.put(workflowInstance.getState(), workflowInstanceMap.getOrDefault(workflowInstance.getState(), 0L) + 1);
        }
        List<String> tableHeaders = Lists.newArrayList("Workflow Instance Status", "Count");
        List<List<String>> tableRows = Lists.newArrayList();
        for (Map.Entry<String, Long> entry : workflowInstanceMap.entrySet()) {
            tableRows.add(Lists.newArrayList(entry.getKey(), String.valueOf(entry.getValue())));
        }
        RowTable rowTable = new RowTable(tableHeaders, tableRows);
        return TableFormatUtils.formatTable(rowTable);
    }
}
