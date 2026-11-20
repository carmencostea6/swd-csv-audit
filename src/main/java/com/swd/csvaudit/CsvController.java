package com.swd.csvaudit;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CsvController {

    private final CsvAnalyzerService csvAnalyzerService;

    public CsvController(CsvAnalyzerService csvAnalyzerService) {
        this.csvAnalyzerService = csvAnalyzerService;
    }

    @PostMapping(path = "/api/csv/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analyze(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Uploaded file must not be empty."));
        }

        try {
            CsvReport report = csvAnalyzerService.analyze(file.getInputStream());
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
        } catch (IOException exception) {
            return ResponseEntity.badRequest().body(Map.of("error", "Unable to read uploaded CSV file."));
        }
    }
}
