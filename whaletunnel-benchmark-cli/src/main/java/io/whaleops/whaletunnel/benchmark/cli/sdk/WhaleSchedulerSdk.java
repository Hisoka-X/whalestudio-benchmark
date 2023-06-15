package io.whaleops.whaletunnel.benchmark.cli.sdk;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkEnvConfiguration;
import io.whaleops.whaletunnel.benchmark.cli.model.project.Project;
import io.whaleops.whaletunnel.benchmark.cli.model.workflow.WorkflowDefinition;
import io.whaleops.whaletunnel.benchmark.cli.model.workflow.WorkflowDefinitionExecuteRequest;
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


    public static void executeWorkflowDefinition(WorkflowDefinitionExecuteRequest workflowDefinitionExecuteRequest) {
        try {
            String response = OkHttpUtils.post(
                EXECUTE_WORKFLOW_DEFINITION_URL,
                generateHeaders(),
                Collections.emptyMap(),
                workflowDefinitionExecuteRequest);
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
            List<Project> projects = JsonUtils.toObject(response, new TypeReference<List<Project>>() {
            });
            return projects;
        } catch (IOException e) {
            throw new RuntimeException("List projects error", e);
        }
    }

    public static List<WorkflowDefinition> listWorkflows(List<Long> projectCodes) {
        try {
            String response = OkHttpUtils.get(LIST_WORKFLOW_DEFINITIONS_URL,
                generateHeaders(),
                Collections.singletonMap("projectCodes", projectCodes)
            );
            return JsonUtils.toObject(response, new TypeReference<List<WorkflowDefinition>>() {
            });
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
