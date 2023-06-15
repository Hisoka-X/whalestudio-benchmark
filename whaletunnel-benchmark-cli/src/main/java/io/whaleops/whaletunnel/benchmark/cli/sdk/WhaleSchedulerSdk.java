package io.whaleops.whaletunnel.benchmark.cli.sdk;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkConfiguration;
import io.whaleops.whaletunnel.benchmark.cli.model.WorkflowDefinitionExecuteRequest;
import io.whaleops.whaletunnel.benchmark.cli.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WhaleSchedulerSdk {

    private static final String WHALESCHEDULER_HOST = WhaleTunnelBenchmarkConfiguration.getProperty("WHALESCHEDULER_HOST", "http://localhost:12345");
    private static final String WHALESCHEDULER_TOKEN = WhaleTunnelBenchmarkConfiguration.getProperty("WHALESCHEDULER_TOKEN");

    private static final String EXECUTE_WORKFLOW_DEFINITION_URL = WHALESCHEDULER_HOST + "/dolphinscheduler/projects/executors/start-process-instance";


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

    private static Map<String, String> generateHeaders() {
        if (WHALESCHEDULER_TOKEN == null) {
            throw new IllegalArgumentException("WHALESCHEDULER_TOKEN is null");
        }
        return new ImmutableMap.Builder<String, String>()
            .put("token", WHALESCHEDULER_TOKEN)
            .build();
    }


}
