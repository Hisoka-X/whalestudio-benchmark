package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
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

    @ShellMethod(key = "dwfi", value = "Clear workflow instance under given projects, e.g. dwfi -p 1,2,3")
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
}
