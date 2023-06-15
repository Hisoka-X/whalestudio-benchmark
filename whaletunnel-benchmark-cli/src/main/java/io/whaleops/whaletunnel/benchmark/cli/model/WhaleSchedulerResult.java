package io.whaleops.whaletunnel.benchmark.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhaleSchedulerResult<T> {

    private Integer code;
    private String message;
    private T data;

}
