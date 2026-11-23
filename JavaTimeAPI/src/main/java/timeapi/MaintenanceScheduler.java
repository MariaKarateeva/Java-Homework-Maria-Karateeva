package timeapi;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {

    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public MaintenanceWindow nextWindow(
            LocalDate date, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(date, "date must not be null");
        Objects.requireNonNull(localStart, "localStart must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
        Objects.requireNonNull(zone, "zone must not be null");
        Objects.requireNonNull(policy, "policy must not be null");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }

        LocalDateTime local = LocalDateTime.of(date, localStart);

        ZoneOffsetTransition transition = zone.getRules().getTransition(local);
        if (transition != null && transition.isGap()) {
            throw new DateTimeException(
                    "Local time " + local + " does not exist in " + zone + " (DST gap)");
        }

        ZonedDateTime start = ZonedDateTime.of(local, zone);
        if (policy == OverlapPolicy.EARLIER) {
            start = start.withEarlierOffsetAtOverlap();
        } else {
            start = start.withLaterOffsetAtOverlap();
        }

        Instant startInstant = start.toInstant();
        return new MaintenanceWindow(startInstant, startInstant.plus(duration));
    }

    public List<MaintenanceWindow> upcomingWindows(
            int days, LocalTime localStart, Duration duration,
            ZoneId zone, OverlapPolicy policy) {
        Objects.requireNonNull(zone, "zone must not be null");

        if (days < 0) {
            throw new IllegalArgumentException("days must not be negative");
        }

        List<MaintenanceWindow> windows = new ArrayList<>();
        LocalDate date = LocalDate.now(clock.withZone(zone));

        for (int i = 0; i < days; i++) {
            windows.add(nextWindow(date, localStart, duration, zone, policy));
            date = date.plusDays(1);
        }
        return windows;
    }
}