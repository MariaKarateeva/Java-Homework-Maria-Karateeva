package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

/**
 * Методы для поиска студентов в картах.
 */
public class StudentRegistry {

    /**
     * Находит студентов, у которых оценка от minGrade до maxGrade (обе границы входят).
     * Возвращает новый список, карта не меняется.
     * Если minGrade больше maxGrade или границы вылезают за 0..10, будет IllegalArgumentException.
     */
    public static List<Student> findStudentsByGradeRange(
            Map<Integer, Student> students,
            double minGrade,
            double maxGrade) {
        if (minGrade > maxGrade || minGrade < 0.0 || maxGrade > 10.0) {
            throw new IllegalArgumentException("Некорректный диапазон оценок");
        }
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
                result.add(student);
            }
        }
        return result;
    }

    /**
     * Возвращает первых n студентов из карты (у них самые большие id, от большего к меньшему).
     * Карта должна быть отсортирована по убыванию ключей. Если студентов меньше n, вернёт всех.
     * Возвращает новый список, карта не меняется.
     * Если n меньше 0, будет IllegalArgumentException.
     */
    public static List<Student> getTopNStudentsById(
            NavigableMap<Integer, Student> students,
            int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n не может быть отрицательным");
        }
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (result.size() == n) {
                break;
            }
            result.add(student);
        }
        return result;
    }
}