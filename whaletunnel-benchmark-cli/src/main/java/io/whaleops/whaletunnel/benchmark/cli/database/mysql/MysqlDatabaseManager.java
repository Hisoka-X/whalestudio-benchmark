package io.whaleops.whaletunnel.benchmark.cli.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import io.whaleops.whaletunnel.benchmark.cli.database.DatabaseManager;

public class MysqlDatabaseManager implements DatabaseManager {

    private final String jdbcUrl;
    private final String userName;
    private final String password;

    public MysqlDatabaseManager(String jdbcUrl, String userName, String password) {
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public List<String> getTables(String databaseName) throws SQLException {
        String sql = "select * from information_schema.TABLES where TABLE_SCHEMA = '?'";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, databaseName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<String> tables = new ArrayList<>();
                while (resultSet.next()) {
                    tables.add(resultSet.getString("TABLE_NAME"));
                }
                return tables;
            }
        }
    }

    public Long getRowCount(String databaseName, String tableName) throws SQLException {
        String sql = "select count(*) from " + databaseName + "." + tableName;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
                return 0L;
            }
        }
    }

    public Map<Long, Map<String, Object>> queryData(String databaseName, String tableName, int index, int limit) throws SQLException {
        String sql = "select * from " + databaseName + "." + tableName + " order by id limit ?, ?";
        Map<Long, Map<String, Object>> result = Maps.newHashMap();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, index);
            preparedStatement.setInt(2, limit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    List<String> columnNames = getColumnNames(metaData);
                    Map<String, Object> row = Maps.newHashMap();
                    for (String columnName : columnNames) {
                        Object columnValue = resultSet.getObject(columnName);
                        if (columnValue instanceof Date || columnValue instanceof LocalDateTime) {
                            continue;
                        }
                        if (columnValue instanceof byte[]) {
                            columnValue = new String((byte[]) columnValue);
                        }
                        row.put(columnName, columnValue);
                    }
                    result.put(resultSet.getLong("id"), row);
                }
            }
        }
        return result;
    }

    private List<String> getColumnNames(ResultSetMetaData metaData) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        return columnNames;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            jdbcUrl,
            userName,
            password);
    }
}
