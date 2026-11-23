package timeapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class IncidentAnalyticsTest {

    private static Incident incident(String id, String start, Duration duration) {
        Instant startedAt = Instant.parse(start);
        return new Incident(id, "svc", startedAt, startedAt.plus(duration));
    }

    @Test
    void totalAndAverageDowntime() {
        List<Incident> incidents = List.of(
                incident("A", "2024-01-01T00:00:00Z", Duration.ofSeconds(1)),
                incident("B", "2024-01-02T00:00:00Z", Duration.ofSeconds(2)),
                incident("C", "2024-01-03T00:00:00Z", Duration.ofSeconds(2)));

        assertEquals(Duration.ofSeconds(5), IncidentAnalytics.totalDowntime(incidents));
        assertEquals(Duration.ofSeconds(1, 666_666_666), IncidentAnalytics.averageDowntime(incidents));
    }

    @Test
    void emptyList() {
        List<Incident> empty = List.of();

        assertEquals(Duration.ZERO, IncidentAnalytics.totalDowntime(empty));
        assertEquals(Duration.ZERO, IncidentAnalytics.averageDowntime(empty));
        assertTrue(IncidentAnalytics.longestIncident(empty).isEmpty());
    }

    @Test
    void longestIncidentIsFound() {
        Incident shortOne = incident("A", "2024-01-01T00:00:00Z", Duration.ofMinutes(10));
        Incident longOne = incident("B", "2024-01-02T00:00:00Z", Duration.ofMinutes(30));

        assertEquals(longOne, IncidentAnalytics.longestIncident(List.of(shortOne, longOne)).get());
    }

    @Test
    void longestIncidentTieBreaksByStartThenId() {
        Incident later = incident("A", "2024-01-02T00:00:00Z", Duration.ofMinutes(30));
        Incident earlierB = incident("B", "2024-01-01T00:00:00Z", Duration.ofMinutes(30));
        Incident earlierA = incident("A2", "2024-01-01T00:00:00Z", Duration.ofMinutes(30));

        assertEquals(earlierA, IncidentAnalytics.longestIncident(List.of(later, earlierB, earlierA)).get());
    }

    @Test
    void groupsByLocalDateInZone() {
        ZoneId berlin = ZoneId.of("Europe/Berlin");
        Incident inc1 = incident("INC-1", "2024-03-30T22:30:00Z", Duration.ofMinutes(150));
        Incident inc2 = incident("INC-2", "2024-03-30T23:30:00Z", Duration.ofMinutes(45));
        Incident inc0 = incident("INC-0", "2024-03-30T22:30:00Z", Duration.ofMinutes(5));

        Map<LocalDate, List<Incident>> result =
                IncidentAnalytics.groupByStartDate(List.of(inc2, inc1, inc0), berlin);

        assertEquals(List.of(LocalDate.of(2024, 3, 30), LocalDate.of(2024, 3, 31)),
                List.copyOf(result.keySet()));
        assertEquals(List.of(inc0, inc1), result.get(LocalDate.of(2024, 3, 30)));
        assertEquals(List.of(inc2), result.get(LocalDate.of(2024, 3, 31)));

        assertEquals(1, IncidentAnalytics.groupByStartDate(List.of(inc2, inc1, inc0), ZoneId.of("UTC")).size());
    }

    @Test
    void doesNotChangeInputList() {
        Incident a = incident("B", "2024-01-02T00:00:00Z", Duration.ofMinutes(1));
        Incident b = incident("A", "2024-01-01T00:00:00Z", Duration.ofMinutes(1));
        List<Incident> input = new java.util.ArrayList<>(List.of(a, b));

        IncidentAnalytics.groupByStartDate(input, ZoneId.of("UTC"));

        assertEquals(List.of(a, b), input);
    }
}