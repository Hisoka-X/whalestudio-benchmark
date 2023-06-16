package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import io.whaleops.whaletunnel.benchmark.cli.model.workflowinstance.WorkflowInstance;
import io.whaleops.whaletunnel.benchmark.cli.sdk.WhaleSchedulerSdk;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class WorkflowInstanceCommand {

    @ShellMethod(key = "delete-wfi", value = "Clear workflow instance under given projects, e.g. delete-wfi -p 1,2,3")
    public void clearWorkflowInstance(@ShellOption(value = "p", help = "projectCodes") List<Long> projectCodes) {
        List<WorkflowInstance> workflowInstances = WhaleSchedulerSdk.listWorkflowInstances(projectCodes);
        if (CollectionUtils.isEmpty(workflowInstances)) {
            log.warn("There is no workflow instance under given projects: {}", projectCodes);
            return;
        }
        String workflowInstanceIds = workflowInstances.stream()
            .map(WorkflowInstance::getId)
            .map(String::valueOf)
            .collect(Collectors.joining(","));
        WhaleSchedulerSdk.deleteWorkflowInstance(workflowInstanceIds);
        log.info("Success delete workflow instances: {} under given project", workflowInstances.size());
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Workflow Instance Status:").append("\t").append("Count").append("\n");
        for (Map.Entry<String, Long> entry : workflowInstanceMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append("\t").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }
}
