/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.whaleops.whaletunnel.benchmark.cli.commands;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Disabled
public class TableCommandTest {

    @Test
    void tableCreate() throws SQLException {
        TableGenerateCommand tableGenerateCommand = new TableGenerateCommand();
        tableGenerateCommand.tableGenerate("oracle", "engine_test", "all_types_table_", 10);
    }

    @Test
    void dataGenerate() throws SQLException {
        DataGenerateCommand tableGenerateCommand = new DataGenerateCommand();
        tableGenerateCommand.dataGenerate("oracle", "engine_test", "ALL_TYPES_TABLE_.*", 100);
    }

}
