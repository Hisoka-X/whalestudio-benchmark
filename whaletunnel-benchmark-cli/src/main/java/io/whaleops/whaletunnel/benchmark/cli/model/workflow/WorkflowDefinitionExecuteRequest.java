package io.whaleops.whaletunnel.benchmark.cli.model.workflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDefinitionExecuteRequest {

    private Long projectCode;
    private Long processDefinitionCode;
    private String scheduleTime = "{\"complementStartDate\":\"2023-06-15 00:00:00\",\"complementEndDate\":\"2023-06-15 00:00:00\",\"complementCalendarType\":\"NATURAL\",\"complementCalendarCode\":\"\"}";
    private String failureStrategy = "CONTINUE";
    private String startNodeList = null;
    private String taskDependType = "TASK_POST";
    private String execType = "START_PROCESS";
    private String warningType = "NONE";
    private Integer warningGroupId = null;
    private String runMode = "RUN_MODE_SERIAL";
    private String processInstancePriority = "MEDIUM";
    private String workerGroup = "default";
    private Long environmentCode = null;
    private String startParams = null;
    private Integer expectedParallelismNumber = null;
    private int dryRun = 0;
    private Long operationId = null;
}
