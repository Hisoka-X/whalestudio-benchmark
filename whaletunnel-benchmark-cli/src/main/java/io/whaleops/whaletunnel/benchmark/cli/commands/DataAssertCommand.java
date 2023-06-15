package io.whaleops.whaletunnel.benchmark.cli.commands;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import io.whaleops.whaletunnel.benchmark.cli.database.mysql.MysqlDatabaseManager;
import io.whaleops.whaletunnel.benchmark.cli.exception.CommandException;
import io.whaleops.whaletunnel.benchmark.cli.exception.DataAssertExceptionStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ShellComponent
public class DataAssertCommand {

    @Autowired
    private MysqlDatabaseManager mysqlDatabaseManager;

    @ShellMethod("Assert if the two table are equals")
    public void assertTableEquals(String originDatabase,
                                  String originTable,
                                  String targetDatabase,
                                  String targetTable) throws SQLException {
        long originTableRowCount = mysqlDatabaseManager.getRowCount(originDatabase, originTable);
        long targetTableRowCount = mysqlDatabaseManager.getRowCount(targetDatabase, targetTable);
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
            Map<Long, Map<String, Object>> originTableMap = mysqlDatabaseManager.queryData(originDatabase, originTable, index, limit);
            Map<Long, Map<String, Object>> targetTableMap = mysqlDatabaseManager.queryData(targetDatabase, targetTable, index, limit);
            Set<Long> ids = originTableMap.keySet();
            for (Long id : ids) {
                Map<String, Object> originRow = originTableMap.get(id);
                Map<String, Object> targetRow = targetTableMap.get(id);
                if (!Objects.equals(originRow, targetRow)) {
                    log.error("The two table are not equals, {}.{} row: {} not equals with {}.{} row: {}",
                        originDatabase,
                        originTable,
                        originRow,
                        targetDatabase,
                        targetTable,
                        targetRow);
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

    @ShellMethod("Assert if the two database are equals, include all tables")
    public void assertDatabaseEquals(String originDatabase, String targetDatabase) throws SQLException {
        // todo: get connection
        List<String> originTables = mysqlDatabaseManager.getTables(originDatabase);
        List<String> targetTables = mysqlDatabaseManager.getTables(targetDatabase);
        if (Objects.equals(originTables, targetTables)) {
            System.out.println("The two database are not equals");
            return;
        }
        for (String table : originTables) {
            assertTableEquals(originDatabase, table, targetDatabase, table);
        }
        log.info("The two database: {} {} are equals!", originDatabase, targetDatabase);
    }

}
