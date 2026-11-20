package com.swd.csvaudit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class CsvAnalyzerService {

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

    public CsvReport analyze(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV input must not be null.");
        }

        try (CSVParser parser = CSV_FORMAT.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String> headers = parser.getHeaderNames();
            if (headers.isEmpty() || headers.stream().allMatch(String::isBlank)) {
                throw new IllegalArgumentException("CSV file is empty or does not contain a header row.");
            }

            int totalRows = 0;
            int emptyCellsCount = 0;
            for (CSVRecord record : parser) {
                if (record.size() != headers.size()) {
                    throw new IllegalArgumentException(
                            "Malformed CSV: row " + (totalRows + 2) + " has " + record.size()
                                    + " columns; expected " + headers.size() + ".");
                }
                totalRows++;
                for (String cell : record) {
                    if (cell.isEmpty()) {
                        emptyCellsCount++;
                    }
                }
            }

            return new CsvReport(headers, totalRows, emptyCellsCount, true);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Unable to parse CSV file: " + exception.getMessage(), exception);
        }
    }
}
