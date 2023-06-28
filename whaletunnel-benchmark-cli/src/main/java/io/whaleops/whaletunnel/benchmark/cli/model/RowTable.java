package io.whaleops.whaletunnel.benchmark.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowTable {

    private List<String> tableHeader;
    private List<List<String>> tableBody;

}
