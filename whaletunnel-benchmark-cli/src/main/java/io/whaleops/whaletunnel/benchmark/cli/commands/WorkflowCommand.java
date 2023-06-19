package io.whaleops.whaletunnel.benchmark.cli.commands;

import static org.springframework.shell.standard.ShellOption.NULL;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import io.whaleops.whaletunnel.benchmark.cli.model.project.Project;
import io.whaleops.whaletunnel.benchmark.cli.model.workflow.WorkflowDefinition;
import io.whaleops.whaletunnel.benchmark.cli.model.workflow.WorkflowDefinitionExecuteRequest;
import io.whaleops.whaletunnel.benchmark.cli.sdk.WhaleSchedulerSdk;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class WorkflowCommand {

    @ShellMethod(value = "Execute a exist workflow, e.g. launch-wf --p 123 --wf 1234", key = "launch-wf")
    public void launchWorkflow(@ShellOption(value = "p", help = "Project Code") Long projectCode,
                               @ShellOption(value = "wf", help = "WorkflowDefinition Code") Long processDefinitionCode) {
        WorkflowDefinitionExecuteRequest workflowDefinitionExecuteRequest = new WorkflowDefinitionExecuteRequest();
        workflowDefinitionExecuteRequest.setProjectCode(projectCode);
        workflowDefinitionExecuteRequest.setProcessDefinitionCode(processDefinitionCode);
        WhaleSchedulerSdk.executeWorkflowDefinition(workflowDefinitionExecuteRequest);
    }

    @ShellMethod(value = "Execute all workflow under exist project, e.g. launch-all-wf --p 123,124", key = "launch-all-wf")
    public void launchAllWorkflowUnderProjects(
        @ShellOption(value = "p", defaultValue = NULL, help = "Project Codes, if empty will launch all workflows") List<Long> projectCodes,
        @ShellOption(value = "b", defaultValue = NULL, help = "Workflow batch size, if set will sleep interval/s after a batch") Integer batchSize,
        @ShellOption(value = "i", defaultValue = NULL, help = "Interval/s") Long interval) {
        if (CollectionUtils.isEmpty(projectCodes)) {
            projectCodes = WhaleSchedulerSdk.listProjects()
                .stream()
                .map(Project::getCode)
                .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(projectCodes)) {
            log.info("There is no project, please create project first");
            return;
        }
        for (Long projectCode : projectCodes) {
            List<WorkflowDefinition> workflowDefinitions = WhaleSchedulerSdk.listWorkflows(projectCodes);
            if (CollectionUtils.isEmpty(workflowDefinitions)) {
                log.info("There is no workflow under project: {}, please create workflow first", projectCode);
                continue;
            }
            int i = 0;
            for (WorkflowDefinition workflowDefinition : workflowDefinitions) {
                launchWorkflow(projectCode, workflowDefinition.getCode());
                log.info("Launch workflow: {} success", workflowDefinition.getName());
                i++;
                if (batchSize == null) {
                    continue;
                }
                if (i % batchSize == 0) {
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(interval));
                    } catch (InterruptedException e) {
                        log.error("Sleep error", e);
                    }
                }
            }
        }
    }
}
