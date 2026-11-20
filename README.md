# swd-csv-audit

Minimal Java 21 / Spring Boot web application for auditing uploaded CSV files.

## Run

```sh
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080), choose a CSV file, and select **Analyze CSV**.
The analysis endpoint is `POST /api/csv/analyze` and accepts a multipart `file` field.

## Test

```sh
mvn test
```
