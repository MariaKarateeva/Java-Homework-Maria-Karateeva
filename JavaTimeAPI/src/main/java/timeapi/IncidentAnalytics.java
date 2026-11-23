package timeapi;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class IncidentAnalytics {

    public static Duration totalDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents must not be null");

        Duration total = Duration.ZERO;
        for (Incident incident : incidents) {
            total = total.plus(incident.duration());
        }
        return total;
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents must not be null");

        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }
        return totalDowntime(incidents).dividedBy(incidents.size());
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents must not be null");

        Comparator<Incident> comparator = Comparator
                .comparing(Incident::duration)
                .thenComparing(Incident::startedAt, Comparator.reverseOrder())
                .thenComparing(Incident::id, Comparator.reverseOrder());

        return incidents.stream().max(comparator);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(
            List<Incident> incidents, ZoneId zone) {
        Objects.requireNonNull(incidents, "incidents must not be null");
        Objects.requireNonNull(zone, "zone must not be null");

        Comparator<Incident> order = Comparator
                .comparing(Incident::startedAt)
                .thenComparing(Incident::id);

        return incidents.stream()
                .sorted(order)
                .collect(Collectors.groupingBy(
                        incident -> incident.startedAt().atZone(zone).toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()));
    }
}