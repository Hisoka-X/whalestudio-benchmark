package io.whaleops.whaletunnel.benchmark.cli.database.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.whaleops.whaletunnel.benchmark.cli.database.DatabaseManager;

public class PgDatabaseManager implements DatabaseManager {

    private final String jdbcUrl;
    private final String userName;
    private final String password;

    public PgDatabaseManager(String jdbcUrl, String userName, String password) {
        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;
    }

    public static final Set<String> POSTGRESQL_SYSTEM_DATABASES =
        Sets.newHashSet(
            "information_schema",
            "pg_catalog",
            "root",
            "pg_toast",
            "pg_temp_1",
            "pg_toast_temp_1",
            "postgres",
            "template0",
            "template1");

    @Override
    public List<String> getTables(String databaseName) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        String query = "SELECT table_schema, table_name FROM information_schema.tables";
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String schemaName = resultSet.getString("table_schema");
                    String tableName = resultSet.getString("table_name");
                    if (schemaName != null && !POSTGRESQL_SYSTEM_DATABASES.contains(schemaName)) {
                        tableNames.add(schemaName + "." + tableName);
                    }
                }
            }
            return tableNames;
        }
    }

    @Override
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

    @Override
    public Map<Long, Map<String, Object>> queryData(String databaseName, String tableName, int index, int limit) throws SQLException {
        String sql = "select * from " + databaseName + "." + tableName + " order by id limit ? offset ?";
        Map<Long, Map<String, Object>> result = Maps.newHashMap();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, index);
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
                        if(columnValue instanceof byte[]) {
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
