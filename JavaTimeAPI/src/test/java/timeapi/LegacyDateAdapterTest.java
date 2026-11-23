package timeapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;

class LegacyDateAdapterTest {

    @Test
    void dateAndInstantRoundTrip() {
        Instant instant = Instant.parse("2024-03-31T01:00:00.123Z");

        Date date = LegacyDateAdapter.toDate(instant);

        assertEquals(instant.toEpochMilli(), date.getTime());
        assertEquals(instant, LegacyDateAdapter.toInstant(date));
    }

    @Test
    void dateToInstantAndBack() {
        Date date = new Date(1711846800000L);

        assertEquals(date, LegacyDateAdapter.toDate(LegacyDateAdapter.toInstant(date)));
    }

    @Test
    void calendarRoundTripKeepsInstantAndZone() {
        ZonedDateTime original = ZonedDateTime.of(2024, 3, 31, 3, 0, 0, 0, ZoneId.of("Europe/Berlin"));

        GregorianCalendar calendar = LegacyDateAdapter.toGregorianCalendar(original);
        ZonedDateTime result = LegacyDateAdapter.toZonedDateTime(calendar);

        assertEquals(original.toInstant(), result.toInstant());
        assertEquals(original.getZone(), result.getZone());
        assertEquals(original, result);
    }
}
