package io.whaleops.whaletunnel.benchmark.cli.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseManager {

    List<String> getTables(String databaseName) throws SQLException;

    Long getRowCount(String databaseName, String tableName) throws SQLException;

    Map<Long, Map<String, Object>> queryData(String databaseName, String tableName, int index, int limit) throws SQLException;

}
