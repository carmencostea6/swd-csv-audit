package com.swd.csvaudit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class CsvAnalyzerServiceTest {

    private final CsvAnalyzerService service = new CsvAnalyzerService();

    @Test
    void parsesValidCsv() {
        CsvReport report = analyze("name,age\nAda,36\nGrace,42\n");

        assertEquals(java.util.List.of("name", "age"), report.headers());
        assertEquals(2, report.totalRows());
        assertEquals(0, report.emptyCellsCount());
        assertTrue(report.isValid());
    }

    @Test
    void countsEmptyCells() {
        CsvReport report = analyze("name,age,city\nAda,,London\n,42,\n");

        assertEquals(3, report.emptyCellsCount());
    }

    @Test
    void rejectsEmptyCsv() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> analyze(""));

        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void rejectsMalformedCsvWithInconsistentColumnCount() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> analyze("name,age\nAda,36,London\n"));

        assertTrue(exception.getMessage().contains("Malformed CSV"));
    }

    private CsvReport analyze(String csv) {
        return service.analyze(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));
    }
}
