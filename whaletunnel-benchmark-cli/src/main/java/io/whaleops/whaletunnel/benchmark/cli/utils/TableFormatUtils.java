package io.whaleops.whaletunnel.benchmark.cli.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import io.whaleops.whaletunnel.benchmark.cli.model.RowTable;

public class TableFormatUtils {

    public static String formatTable(RowTable rowTable) {
        List<String> tableHeader = rowTable.getTableHeader();
        List<List<String>> tableBody = rowTable.getTableBody();
        if (CollectionUtils.isEmpty(tableHeader)) {
            throw new IllegalArgumentException("Table header is empty");
        }
        int[] columnWidths = new int[tableHeader.size()];
        for (int i = 0; i < tableHeader.size(); i++) {
            columnWidths[i] = tableHeader.get(i).length();
        }
        if (CollectionUtils.isNotEmpty(tableBody)) {
            for (List<String> row : tableBody) {
                for (int i = 0; i < row.size(); i++) {
                    if (row.get(i) == null) {
                        continue;
                    }
                    columnWidths[i] = Math.max(columnWidths[i], row.get(i).length());
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        for (int columnWidth : columnWidths) {
            sb.append("-".repeat(Math.max(0, columnWidth + 2)));
            sb.append("+");
        }
        sb.append("\n");
        sb.append("|");
        for (int i = 0; i < tableHeader.size(); i++) {
            sb.append(" ");
            sb.append(tableHeader.get(i));
            sb.append(" ".repeat(Math.max(0, columnWidths[i] - tableHeader.get(i).length())));
            sb.append(" |");
        }
        sb.append("\n");
        sb.append("+");
        for (int columnWidth : columnWidths) {
            sb.append("-".repeat(Math.max(0, columnWidth + 2)));
            sb.append("+");
        }
        sb.append("\n");
        if (CollectionUtils.isNotEmpty(tableBody)) {
            for (List<String> row : tableBody) {
                sb.append("|");
                for (int i = 0; i < row.size(); i++) {
                    sb.append(" ");
                    sb.append(row.get(i));
                    sb.append(" ".repeat(Math.max(0, columnWidths[i] - row.get(i).length())));
                    sb.append(" |");
                }
                sb.append("\n");
            }
        }
        sb.append("+");
        for (int columnWidth : columnWidths) {
            sb.append("-".repeat(Math.max(0, columnWidth + 2)));
            sb.append("+");
        }
        sb.append("\n");
        return sb.toString();
    }
}
