package homework;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Сравнивает скорость ArrayList и LinkedList на шести операциях.
 * Запускается через main и печатает таблицу с результатами.
 */
public class CollectionPerformanceExperiment {

    private static final int SIZE = 10000;
    private static final int WARMUP_RUNS = 2;
    private static final int MEASURE_RUNS = 5;

    private static final String[] SCENARIO_NAMES = {
            "Добавление в конец",
            "Добавление в начало",
            "Вставка в середину",
            "Доступ по индексу",
            "Удаление из начала",
            "Удаление из конца"
    };

    private static long checksum = 0;

    /**
     * Запускает эксперимент и печатает таблицу.
     */
    public static void main(String[] args) {
        System.out.printf("%-22s %10s %16s %16s%n",
                "Операция", "Элементов", "ArrayList, мс", "LinkedList, мс");

        for (int scenario = 0; scenario < SCENARIO_NAMES.length; scenario++) {
            double arrayListTime = measure(scenario, false);
            double linkedListTime = measure(scenario, true);
            System.out.printf("%-22s %10d %16.3f %16.3f%n",
                    SCENARIO_NAMES[scenario], SIZE, arrayListTime, linkedListTime);
        }

        System.out.println("Контрольная сумма: " + checksum);
    }

    private static double measure(int scenario, boolean linked) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            runScenario(scenario, linked);
        }

        long total = 0;
        for (int i = 0; i < MEASURE_RUNS; i++) {
            total += runScenario(scenario, linked);
        }
        return total / (double) MEASURE_RUNS / 1_000_000.0;
    }

    // Один запуск сценария на новом списке. Возвращает время в наносекундах.
    // Заполнение списка перед замером во время не входит.
    private static long runScenario(int scenario, boolean linked) {
        List<Integer> list;
        if (linked) {
            list = new LinkedList<>();
        } else {
            list = new ArrayList<>();
        }

        // Для чтения и удаления список должен быть заранее заполнен
        if (scenario >= 3) {
            for (int i = 0; i < SIZE; i++) {
                list.add(i);
            }
        }

        long start = System.nanoTime();

        if (scenario == 0) {
            for (int i = 0; i < SIZE; i++) {
                list.add(i);
            }
        } else if (scenario == 1) {
            for (int i = 0; i < SIZE; i++) {
                list.add(0, i);
            }
        } else if (scenario == 2) {
            for (int i = 0; i < SIZE; i++) {
                list.add(list.size() / 2, i);
            }
        } else if (scenario == 3) {
            for (int i = 0; i < SIZE; i++) {
                checksum += list.get(i);
            }
        } else if (scenario == 4) {
            while (!list.isEmpty()) {
                checksum += list.remove(0);
            }
        } else {
            while (!list.isEmpty()) {
                checksum += list.remove(list.size() - 1);
            }
        }

        long end = System.nanoTime();

        checksum += list.size();
        return end - start;
    }
}