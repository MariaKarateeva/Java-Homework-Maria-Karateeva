package timeapi;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class IncidentParser {

    public static Incident parse(String line) {
        Objects.requireNonNull(line, "line must not be null");

        String[] parts = line.split(";", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "Expected 4 fields (id;service;startedAt;resolvedAt), got " + parts.length);
        }

        Instant startedAt = parseInstant(parts[2], "startedAt");
        Instant resolvedAt = parseInstant(parts[3], "resolvedAt");

        return new Incident(parts[0].trim(), parts[1].trim(), startedAt, resolvedAt);
    }

    private static Instant parseInstant(String value, String fieldName) {
        try {
            return Instant.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldName + ": '" + value + "'", e);
        }
    }
}