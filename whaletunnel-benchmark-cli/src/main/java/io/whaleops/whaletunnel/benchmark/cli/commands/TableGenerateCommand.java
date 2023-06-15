package io.whaleops.whaletunnel.benchmark.cli.commands;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import io.whaleops.whaletunnel.benchmark.cli.configuration.WhaleTunnelBenchmarkMysqlDatabaseConfiguration;

/**
 * Use to generate table for benchmark.
 */
@ShellComponent
public class TableGenerateCommand {

    private String sqlTemplete = "CREATE TABLE %s ( `id` int(11) NOT NULL AUTO_INCREMENT," +
        " `smallint_col` smallint(6) DEFAULT NULL, `integer_col` int(11) DEFAULT NULL, `bigint_col` bigint(20) DEFAULT NULL," +
        " `decimal_col` decimal(10,2) DEFAULT NULL, `numeric_col` decimal(10,2) DEFAULT NULL, `real_col` float DEFAULT NULL," +
        " `double_col` double DEFAULT NULL, `smallserial_col` smallint(6) DEFAULT NULL, `serial_col` int(11) DEFAULT NULL," +
        " `bigserial_col` bigint(20) DEFAULT NULL, `varchar_col` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL," +
        " `text_col` text COLLATE utf8mb4_unicode_ci, `date_col` date DEFAULT NULL, `time_col` time DEFAULT NULL," +
        " `timestamp_col` datetime DEFAULT NULL, `boolean_col` tinyint(1) DEFAULT NULL, `bytea_col` longblob," +
        " PRIMARY KEY (`id`) ) ENGINE=InnoDB AUTO_INCREMENT=2112257 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";

    @ShellMethod(key = "table")
    public String tableGenerate(String database, @ShellOption(defaultValue = "all_types_table_") String prefix, int number) throws SQLException {

        WhaleTunnelBenchmarkMysqlDatabaseConfiguration instance = WhaleTunnelBenchmarkMysqlDatabaseConfiguration.getInstance();

        String url = instance.getJdbcUrl();
        String user = instance.getUserName();
        String password = instance.getPassword();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            for (int i = 1; i <= number; i++) {
                String sql = String.format(sqlTemplete, String.format(database + ".`" + prefix + "%04d`", i));
                statement.executeUpdate(sql);
            }
            return "success with " + number + " tables";
        }
    }

}
