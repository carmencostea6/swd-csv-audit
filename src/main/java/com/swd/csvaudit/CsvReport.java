package com.swd.csvaudit;

import java.util.List;

public record CsvReport(List<String> headers, int totalRows, int emptyCellsCount, boolean isValid) {

    public CsvReport {
        headers = List.copyOf(headers);
    }
}
