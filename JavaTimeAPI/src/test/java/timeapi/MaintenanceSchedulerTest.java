package timeapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class MaintenanceSchedulerTest {

    private static final ZoneId BERLIN = ZoneId.of("Europe/Berlin");

    private final Clock clock = Clock.fixed(Instant.parse("2024-03-30T00:00:00Z"), ZoneOffset.UTC);
    private final MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

    @Test
    void rejectsLocalTimeInGap() {
        assertThrows(DateTimeException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 31), LocalTime.of(2, 30),
                Duration.ofHours(1), BERLIN, OverlapPolicy.EARLIER));
    }

    @Test
    void overlapPolicyChoosesOffset() {
        LocalDate date = LocalDate.of(2024, 10, 27);
        LocalTime time = LocalTime.of(2, 30);

        MaintenanceWindow earlier = scheduler.nextWindow(date, time, Duration.ofHours(1), BERLIN, OverlapPolicy.EARLIER);
        MaintenanceWindow later = scheduler.nextWindow(date, time, Duration.ofHours(1), BERLIN, OverlapPolicy.LATER);

        assertEquals(Instant.parse("2024-10-27T00:30:00Z"), earlier.start());
        assertEquals(Instant.parse("2024-10-27T01:30:00Z"), later.start());
        assertEquals(ZoneOffset.ofHours(2), earlier.start().atZone(BERLIN).getOffset());
        assertEquals(ZoneOffset.ofHours(1), later.start().atZone(BERLIN).getOffset());
        assertEquals(Duration.ofHours(1), Duration.between(earlier.start(), later.start()));
    }

    @Test
    void windowDurationIsPhysical() {
        MaintenanceWindow window = scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER);

        assertEquals(Duration.ofMinutes(30), window.duration());
        assertEquals(window.start().plus(Duration.ofMinutes(30)), window.end());
    }

    @Test
    void dailyWindowsKeepLocalTimeAcrossDst() {
        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER);

        assertEquals(2, windows.size());

        MaintenanceWindow first = windows.get(0);
        MaintenanceWindow second = windows.get(1);

        assertEquals(LocalDate.of(2024, 3, 30), first.start().atZone(BERLIN).toLocalDate());
        assertEquals(LocalDate.of(2024, 3, 31), second.start().atZone(BERLIN).toLocalDate());
        assertEquals(LocalTime.of(3, 30), first.start().atZone(BERLIN).toLocalTime());
        assertEquals(LocalTime.of(3, 30), second.start().atZone(BERLIN).toLocalTime());
        assertEquals(Duration.ofHours(23), Duration.between(first.start(), second.start()));

        List<MaintenanceWindow> again = scheduler.upcomingWindows(
                2, LocalTime.of(3, 30), Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER);
        assertEquals(windows, again);
    }

    @Test
    void zeroDaysGivesEmptyList() {
        assertTrue(scheduler.upcomingWindows(
                0, LocalTime.of(3, 30), Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER).isEmpty());
    }

    @Test
    void negativeDaysIsError() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.upcomingWindows(
                -1, LocalTime.of(3, 30), Duration.ofMinutes(30), BERLIN, OverlapPolicy.EARLIER));
    }

    @Test
    void nonPositiveDurationIsError() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ZERO, BERLIN, OverlapPolicy.EARLIER));
        assertThrows(IllegalArgumentException.class, () -> scheduler.nextWindow(
                LocalDate.of(2024, 3, 30), LocalTime.of(3, 30),
                Duration.ofMinutes(-5), BERLIN, OverlapPolicy.EARLIER));
    }
}