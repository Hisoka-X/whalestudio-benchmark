package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import io.whaleops.whaletunnel.benchmark.cli.model.WorkflowDefinitionExecuteRequest;
import io.whaleops.whaletunnel.benchmark.cli.sdk.WhaleSchedulerSdk;

@ShellComponent
public class WorkflowExecuteCommand {

    @ShellMethod("Execute a exist workflow")
    public void executeWorkflow(@ShellOption(help = "Project Code") Long projectCode,
                                @ShellOption(help = "WorkflowDefinition Code") Long processDefinitionCode) {
        WorkflowDefinitionExecuteRequest workflowDefinitionExecuteRequest = new WorkflowDefinitionExecuteRequest();
        workflowDefinitionExecuteRequest.setProjectCode(projectCode);
        workflowDefinitionExecuteRequest.setProcessDefinitionCode(processDefinitionCode);
        WhaleSchedulerSdk.executeWorkflowDefinition(workflowDefinitionExecuteRequest);
    }

}
