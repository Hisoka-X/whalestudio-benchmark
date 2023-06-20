package io.whaleops.whaletunnel.benchmark.cli.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import io.whaleops.whaletunnel.benchmark.cli.database.DatabaseManager;
import io.whaleops.whaletunnel.benchmark.cli.database.DatabaseManagerFactory;
import io.whaleops.whaletunnel.benchmark.cli.exception.CommandException;
import io.whaleops.whaletunnel.benchmark.cli.exception.DataAssertExceptionStatus;
import io.whaleops.whaletunnel.benchmark.cli.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class DataAssertCommand {

    @Autowired
    private DatabaseManagerFactory databaseManagerFactory;

    @ShellMethod(
        value = "Assert if the two table are equals, e.g. assert-table --origin-d engine_test  --origin-t public.\"all_types_table_030\" --origin-url jdbc:postgresql://datasource01:5432/engine_test --origin-u postgres --origin-p postgres --target-d engine_test --target-t all_types_table_0030 --target-url jdbc:mysql://datasource01:3306 --target-u root --target-p root@123",
        key = "assert-table")
    public void assertTableEquals(
        @ShellOption(value = "origin-d") String originDatabase,
        @ShellOption(value = "origin-t") String originTable,
        @ShellOption(value = "origin-url") String originJdbcUrl,
        @ShellOption(value = "origin-u") String originUserName,
        @ShellOption(value = "origin-p") String originPassword,
        @ShellOption(value = "target-d") String targetDatabase,
        @ShellOption(value = "target-t") String targetTable,
        @ShellOption(value = "target-url") String targetJdbcUrl,
        @ShellOption(value = "target-u") String targetUserName,
        @ShellOption(value = "target-p") String targetPassword) throws SQLException {
        DatabaseManager originDatabaseManager = databaseManagerFactory.getDatabaseManager(originJdbcUrl, originUserName, originPassword);
        DatabaseManager targetDatabaseManager = databaseManagerFactory.getDatabaseManager(targetJdbcUrl, targetUserName, targetPassword);

        long originTableRowCount = originDatabaseManager.getRowCount(originDatabase, originTable);
        long targetTableRowCount = targetDatabaseManager.getRowCount(targetDatabase, targetTable);
        // assert the data count
        if (originTableRowCount != targetTableRowCount) {
            log.error("The two table are not equals, {}.{} row count: {}, {}.{} row count: {}",
                originDatabase,
                originTable,
                originTableRowCount,
                targetDatabase,
                targetTable,
                targetTableRowCount);
            throw CommandException.newCommandException(DataAssertExceptionStatus.ROW_COUNT_DOES_NOT_MATCH);
        }
        // assert the data
        int index = 0;
        int limit = 10000;
        while (index < originTableRowCount) {
            Map<Long, Map<String, Object>> originTableMap = originDatabaseManager.queryData(originDatabase, originTable, index, limit);
            Map<Long, Map<String, Object>> targetTableMap = targetDatabaseManager.queryData(targetDatabase, targetTable, index, limit);
            Set<Long> ids = originTableMap.keySet();
            for (Long id : ids) {
                Map<String, Object> originRow = originTableMap.get(id);
                Map<String, Object> targetRow = targetTableMap.get(id);
                Set<String> columns = originRow.keySet();
                for (String column : columns) {
                    if (!Objects.equals("" + originRow.get(column), "" + targetRow.get(column))) {
                        log.error("The two table are not equals, {}.{} row: {} not equals with {}.{} row: {}",
                            originDatabase,
                            originTable,
                            JsonUtils.toPrettyJsonString(originRow),
                            targetDatabase,
                            targetTable,
                            JsonUtils.toPrettyJsonString(targetRow));
                        log.error("column {}: {} are not equals with {}", column, originRow.get(column), targetRow.get(column));
                        throw new RuntimeException("The two table are not equals!");
                    }
                }
            }
            index += limit;
        }
        log.info("The two table: {}.{} {}.{} are equals!",
            originDatabase,
            originTable,
            targetDatabase,
            targetTable);
    }

    @ShellMethod(
        value = "Assert if the two database are equals, include all tables. e.g. assert-database --origin-d --origin-url --origin-u --origin-p --target-d --target-url --target-u --target-p",
        key = "assert-database")
    public void assertDatabaseEquals(@ShellOption(value = "origin-d") String originDatabase,
                                     @ShellOption(value = "origin-url") String originJdbcUrl,
                                     @ShellOption(value = "origin-u") String originUserName,
                                     @ShellOption(value = "origin-p") String originPassword,
                                     @ShellOption(value = "target-d") String targetDatabase,
                                     @ShellOption(value = "target-url") String targetJdbcUrl,
                                     @ShellOption(value = "target-u") String targetUserName,
                                     @ShellOption(value = "target-p") String targetPassword) throws SQLException {
        DatabaseManager originDatabaseManager = databaseManagerFactory.getDatabaseManager(originJdbcUrl, originUserName, originPassword);
        DatabaseManager targetDatabaseManager = databaseManagerFactory.getDatabaseManager(targetJdbcUrl, targetUserName, targetPassword);

        List<String> originTables = originDatabaseManager.getTables(originDatabase);
        List<String> targetTables = targetDatabaseManager.getTables(targetDatabase);
        if (Objects.equals(originTables, targetTables)) {
            System.out.println("The two database are not equals");
            return;
        }
        for (String table : originTables) {
            assertTableEquals(originDatabase, table, originJdbcUrl, originUserName, originPassword, targetDatabase, table, targetJdbcUrl, targetUserName, targetPassword);
        }
        log.info("The two database: {} {} are equals!", originDatabase, targetDatabase);
    }

}
