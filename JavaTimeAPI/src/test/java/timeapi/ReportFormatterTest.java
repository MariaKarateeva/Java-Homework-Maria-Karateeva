package timeapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import org.junit.jupiter.api.Test;

class ReportFormatterTest {

    @Test
    void formatsWindowInGivenZone() {
        MaintenanceWindow window = new MaintenanceWindow(
                Instant.parse("2024-03-31T01:00:00Z"),
                Instant.parse("2024-03-31T02:00:00Z"));

        String result = ReportFormatter.formatWindow(window, ZoneId.of("Europe/Berlin"), Locale.ROOT);
        assertEquals("31.03.2024 03:00 +02:00 (Europe/Berlin) – 31.03.2024 04:00 +02:00 (Europe/Berlin)", result);
    }
}
