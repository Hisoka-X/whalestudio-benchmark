package io.whaleops.whaletunnel.benchmark.cli.model.workflowinstance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowInstance {

    private Long id;

    private String name;

    private String state;

}
