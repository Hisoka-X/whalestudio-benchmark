package io.whaleops.whaletunnel.benchmark.cli.sdk;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkEnvConfiguration;
import io.whaleops.whaletunnel.benchmark.cli.model.PageInfo;
import io.whaleops.whaletunnel.benchmark.cli.model.WhaleSchedulerResult;
import io.whaleops.whaletunnel.benchmark.cli.model.project.Project;
import io.whaleops.whaletunnel.benchmark.cli.model.workflow.WorkflowDefinition;
import io.whaleops.whaletunnel.benchmark.cli.model.workflow.WorkflowDefinitionExecuteRequest;
import io.whaleops.whaletunnel.benchmark.cli.model.workflowinstance.WorkflowInstance;
import io.whaleops.whaletunnel.benchmark.cli.utils.JsonUtils;
import io.whaleops.whaletunnel.benchmark.cli.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhaleSchedulerSdk {

    private static final String WHALESCHEDULER_HOST = WhaleTunnelBenchmarkEnvConfiguration.getInstance().getWhaleSchedulerHost();
    private static final String WHALESCHEDULER_TOKEN = WhaleTunnelBenchmarkEnvConfiguration.getInstance().getWhaleSchedulerToken();

    private static final String EXECUTE_WORKFLOW_DEFINITION_URL = WHALESCHEDULER_HOST + "/dolphinscheduler/projects/executors/start-process-instance";
    private static final String LIST_PROJECTS_URL = WHALESCHEDULER_HOST + "/dolphinscheduler/projects/list";
    private static final String LIST_WORKFLOW_DEFINITIONS_URL = WHALESCHEDULER_HOST + "/dolphinscheduler/projects/process-definition/query-process-definition-list";
    private static final String LIST_WORKFLOW_INSTANCES_URL = WHALESCHEDULER_HOST + "/dolphinscheduler/projects/process-instances";
    private static final String DELETE_WORKFLOW_INSTANCES_URL = WHALESCHEDULER_HOST + "/dolphinscheduler/projects/process-instances/batch-delete";


    public static void executeWorkflowDefinition(WorkflowDefinitionExecuteRequest workflowDefinitionExecuteRequest) {
        try {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("projectCode", workflowDefinitionExecuteRequest.getProjectCode());
            requestParams.put("processDefinitionCode", workflowDefinitionExecuteRequest.getProcessDefinitionCode());
            requestParams.put("scheduleTime", workflowDefinitionExecuteRequest.getScheduleTime());
            requestParams.put("failureStrategy", workflowDefinitionExecuteRequest.getFailureStrategy());
            requestParams.put("startNodeList", workflowDefinitionExecuteRequest.getStartNodeList());
            requestParams.put("taskDependType", workflowDefinitionExecuteRequest.getTaskDependType());
            requestParams.put("execType", workflowDefinitionExecuteRequest.getExecType());
            requestParams.put("warningType", workflowDefinitionExecuteRequest.getWarningType());
            requestParams.put("warningGroupId", workflowDefinitionExecuteRequest.getWarningGroupId());
            requestParams.put("runMode", workflowDefinitionExecuteRequest.getRunMode());
            requestParams.put("processInstancePriority", workflowDefinitionExecuteRequest.getProcessInstancePriority());
            requestParams.put("workerGroup", workflowDefinitionExecuteRequest.getWorkerGroup());
            requestParams.put("environmentCode", workflowDefinitionExecuteRequest.getEnvironmentCode());
            requestParams.put("startParams", workflowDefinitionExecuteRequest.getStartParams());
            requestParams.put("expectedParallelismNumber", workflowDefinitionExecuteRequest.getExpectedParallelismNumber());
            requestParams.put("dryRun", workflowDefinitionExecuteRequest.getDryRun());
            requestParams.put("operationId", workflowDefinitionExecuteRequest.getOperationId());
            String response = OkHttpUtils.post(
                EXECUTE_WORKFLOW_DEFINITION_URL,
                generateHeaders(),
                requestParams,
                null);
            log.info("Execute workflow definition response: {}", response);
        } catch (IOException e) {
            throw new RuntimeException("Execute workflow error", e);
        }
    }

    public static List<Project> listProjects() {
        try {
            String response = OkHttpUtils.get(
                LIST_PROJECTS_URL,
                generateHeaders(),
                Collections.emptyMap());
            WhaleSchedulerResult<List<Project>> listWhaleSchedulerResult = JsonUtils.toObject(response, new TypeReference<WhaleSchedulerResult<List<Project>>>() {
            });
            return listWhaleSchedulerResult.getData();
        } catch (IOException e) {
            throw new RuntimeException("List projects error", e);
        }
    }

    public static List<WorkflowDefinition> listWorkflows(List<Long> projectCodes) {
        try {
            String response = OkHttpUtils.get(LIST_WORKFLOW_DEFINITIONS_URL,
                generateHeaders(),
                Collections.singletonMap("projectCodes", projectCodes.stream().map(String::valueOf).collect(Collectors.joining(",")))
            );
            WhaleSchedulerResult<List<WorkflowDefinition>> listWhaleSchedulerResult = JsonUtils.toObject(response, new TypeReference<WhaleSchedulerResult<List<WorkflowDefinition>>>() {
            });
            return listWhaleSchedulerResult.getData();
        } catch (IOException e) {
            throw new RuntimeException("List workflows error", e);
        }
    }

    public static List<WorkflowInstance> listWorkflowInstances(List<Long> projectCodes) {
        try {
            ImmutableMap<String, Object> requestParams = new ImmutableMap.Builder<String, Object>()
                .put("projectCodes", projectCodes.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .put("pageNo", 1)
                .put("pageSize", 1000)
                .build();
            String response = OkHttpUtils.get(LIST_WORKFLOW_INSTANCES_URL,
                generateHeaders(),
                requestParams
            );
            WhaleSchedulerResult<PageInfo<WorkflowInstance>> listWhaleSchedulerResult = JsonUtils.toObject(response, new TypeReference<WhaleSchedulerResult<PageInfo<WorkflowInstance>>>() {
            });
            return listWhaleSchedulerResult.getData().getTotalList();
        } catch (IOException e) {
            throw new RuntimeException("List workflows error", e);
        }
    }

    public static void deleteWorkflowInstance(String workflowInstanceIds) {
        try {
            ImmutableMap<String, Object> requestParams = new ImmutableMap.Builder<String, Object>()
                .put("processInstanceIds", workflowInstanceIds)
                .build();
            String response = OkHttpUtils.post(DELETE_WORKFLOW_INSTANCES_URL,
                generateHeaders(),
                requestParams,
                null
            );
            WhaleSchedulerResult<Void> voidWhaleSchedulerResult = JsonUtils.toObject(response, new TypeReference<WhaleSchedulerResult<Void>>() {
            });
            if (voidWhaleSchedulerResult.getCode() != 0) {
                throw new RuntimeException("Delete workflow instance error: " + voidWhaleSchedulerResult.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException("List workflows error", e);
        }
    }

    private static Map<String, String> generateHeaders() {
        if (WHALESCHEDULER_TOKEN == null) {
            throw new IllegalArgumentException("WHALESCHEDULER_TOKEN is null");
        }
        return new ImmutableMap.Builder<String, String>()
            .put("token", WHALESCHEDULER_TOKEN)
            .build();
    }
}
