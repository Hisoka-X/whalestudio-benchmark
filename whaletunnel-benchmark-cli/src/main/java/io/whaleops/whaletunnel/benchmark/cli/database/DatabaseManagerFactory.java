package io.whaleops.whaletunnel.benchmark.cli.database;

import org.springframework.stereotype.Component;

import io.whaleops.whaletunnel.benchmark.cli.database.mysql.MysqlDatabaseManager;
import io.whaleops.whaletunnel.benchmark.cli.database.postgresql.PgDatabaseManager;

@Component
public class DatabaseManagerFactory {

    public DatabaseManager getDatabaseManager(String jdbcUrl,
                                              String userName,
                                              String password) {
        if (jdbcUrl.contains("mysql")) {
            return new MysqlDatabaseManager(jdbcUrl, userName, password);
        }

        if (jdbcUrl.contains("postgresql")) {
            return new PgDatabaseManager(jdbcUrl, userName, password);
        }
        throw new IllegalArgumentException("Unsupported database: " + jdbcUrl);
    }

}
