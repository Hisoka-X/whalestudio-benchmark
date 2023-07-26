package io.whaleops.whaletunnel.benchmark.cli.commands;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkOracleDatabaseConfiguration;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkMysqlDatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellOption;

/**
 * Use to generate data for benchmark.
 */
@ShellComponent
@Slf4j
public class DataGenerateCommand {

    private static final String insertSql = "INSERT INTO TEST.%s (smallint_col, integer_col, " +
        "bigint_col, decimal_col, numeric_col, real_col, double_col, smallserial_col, serial_col, bigserial_col," +
        " varchar_col, text_col, date_col, time_col, timestamp_col, boolean_col, bytea_col)" +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @ShellMethod(key = "data")
    public String dataGenerate(@ShellOption(defaultValue = "ORACLE") String type, String database, String tablePattern, int number) throws SQLException {

        String url;
        String user;
        String password;
        if (type.equalsIgnoreCase("ORACLE")) {
//            WhaleTunnelBenchmarkOracleDatabaseConfiguration instance = WhaleTunnelBenchmarkOracleDatabaseConfiguration.getInstance();
//            url = instance.getJdbcUrl();
//            user = instance.getUserName();
//            password = instance.getPassword();

            url = "jdbc:oracle:thin:@localhost:1522:xe";
            user = "test";
            password = "oracle";
        } else {
            WhaleTunnelBenchmarkMysqlDatabaseConfiguration instance = WhaleTunnelBenchmarkMysqlDatabaseConfiguration.getInstance();
            url = instance.getJdbcUrl();
            user = instance.getUserName();
            password = instance.getPassword();
        }
        Random random = new Random();
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             ResultSet resultSet =
                 connection
                     .getMetaData()
                     .getTables(null, null, null, new String[] {"TABLE"})) {
            Pattern pattern = Pattern.compile(tablePattern);
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                if (pattern.matcher(tableName).matches()) {
                    tableNames.add(tableName);
                }
            }
            log.info("find table: " + tableNames);
        }

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            for (String tableName : tableNames) {
                String sql = String.format(insertSql, tableName);

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    for (int i = 1; i <= number; i++) {
                        statement.setShort(1, (short) (i % Short.MAX_VALUE));
                        statement.setInt(2, i);
                        statement.setLong(3, random.nextLong());
                        statement.setBigDecimal(4, new BigDecimal("33333.33"));
                        statement.setBigDecimal(5, new BigDecimal("44444.44"));
                        statement.setFloat(6, random.nextFloat());
                        statement.setDouble(7, random.nextDouble());
                        statement.setShort(8, (short) (i % Short.MAX_VALUE));
                        statement.setInt(9, i);
                        statement.setLong(10, random.nextLong());
                        statement.setString(11, "varchar_col_" + i);
                        statement.setString(12, "text_col_" + i);
                        statement.setDate(13, new java.sql.Date(System.currentTimeMillis()));
                        statement.setTime(14, new java.sql.Time(System.currentTimeMillis()));
                        statement.setTimestamp(15, new java.sql.Timestamp(System.currentTimeMillis()));
                        statement.setBoolean(16, random.nextBoolean());
                        statement.setBytes(17, new byte[]{1, 2, 3, 4, 5});
                        statement.addBatch();
                    }
                    statement.executeBatch();
                    log.info("finished table insert: " + tableName);
                }
            }
        }
        return "success";
    }

}
