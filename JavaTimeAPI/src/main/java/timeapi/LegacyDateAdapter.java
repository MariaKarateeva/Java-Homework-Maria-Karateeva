package timeapi;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

public class LegacyDateAdapter {

    public static Instant toInstant(Date date) {
        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        return Date.from(instant);
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        return calendar.toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime dateTime) {
        return GregorianCalendar.from(dateTime);
    }
}