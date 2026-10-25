package homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentRegistryTest {

    private Student anna;
    private Student boris;
    private Student clara;
    private Student denis;

    private Map<Integer, Student> hashMap;
    private NavigableMap<Integer, Student> treeMap;

    @BeforeEach
    void setUp() {
        anna = new Student(1, "Anna", 4.0);
        boris = new Student(2, "Boris", 7.5);
        clara = new Student(3, "Clara", 7.5);
        denis = new Student(4, "Denis", 10.0);

        hashMap = new HashMap<>();
        treeMap = new TreeMap<>(Collections.reverseOrder());
        Student[] all = {anna, boris, clara, denis};
        for (Student student : all) {
            hashMap.put(student.getId(), student);
            treeMap.put(student.getId(), student);
        }
    }

    @Test
    void studentRejectsWrongId() {
        assertThrows(IllegalArgumentException.class, () -> new Student(0, "Ivan", 5.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(-1, "Ivan", 5.0));
    }

    @Test
    void studentRejectsEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "", 5.0));
    }

    @Test
    void studentRejectsWrongGrade() {
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "Ivan", -0.1));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "Ivan", 10.1));
    }

    @Test
    void equalStudentsHaveEqualHashCodes() {
        Student first = new Student(5, "Olga", 6.0);
        Student second = new Student(5, "Olga", 6.0);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(first, new Student(5, "Olga", 6.5));
    }

    @Test
    void findByRangeIncludesBothBorders() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 7.5, 10.0);

        Set<Student> expected = new HashSet<>();
        expected.add(boris);
        expected.add(clara);
        expected.add(denis);
        assertEquals(expected, new HashSet<>(result));
        assertEquals(3, result.size());
    }

    @Test
    void findByRangeUsualCase() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 5.0, 8.0);

        assertEquals(2, result.size());
        assertTrue(result.contains(boris));
        assertTrue(result.contains(clara));
    }

    @Test
    void findByRangeWithNoMatchesReturnsEmptyList() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 8.0, 9.0);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByRangeInEmptyMapReturnsEmptyList() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(new HashMap<>(), 0.0, 10.0);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByRangeWithSameBorders() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 4.0, 4.0);

        assertEquals(1, result.size());
        assertEquals(anna, result.get(0));
    }

    @Test
    void findByRangeRejectsWrongArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(hashMap, 8.0, 5.0));
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(hashMap, -1.0, 5.0));
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(hashMap, 5.0, 10.5));
    }

    @Test
    void findByRangeDoesNotChangeMap() {
        StudentRegistry.findStudentsByGradeRange(hashMap, 0.0, 10.0);

        assertEquals(4, hashMap.size());
    }

    @Test
    void findByRangeReturnsModifiableList() {
        List<Student> result = StudentRegistry.findStudentsByGradeRange(hashMap, 0.0, 10.0);

        result.add(new Student(100, "Extra", 1.0));
        assertEquals(5, result.size());
        assertEquals(4, hashMap.size());
    }

    @Test
    void topNReturnsStudentsWithBiggestIdInDescendingOrder() {
        List<Student> result = StudentRegistry.getTopNStudentsById(treeMap, 2);

        assertEquals(List.of(denis, clara), result);
    }

    @Test
    void topNWithNBiggerThanMapSizeReturnsAll() {
        List<Student> result = StudentRegistry.getTopNStudentsById(treeMap, 10);

        assertEquals(List.of(denis, clara, boris, anna), result);
    }

    @Test
    void topNWithZeroReturnsEmptyList() {
        List<Student> result = StudentRegistry.getTopNStudentsById(treeMap, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void topNWithNegativeNThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.getTopNStudentsById(treeMap, -1));
    }

    @Test
    void topNInEmptyMapReturnsEmptyList() {
        NavigableMap<Integer, Student> empty = new TreeMap<>(Collections.reverseOrder());

        assertTrue(StudentRegistry.getTopNStudentsById(empty, 3).isEmpty());
    }

    @Test
    void topNDoesNotChangeMapAndReturnsModifiableList() {
        List<Student> result = StudentRegistry.getTopNStudentsById(treeMap, 2);

        result.clear();
        assertEquals(4, treeMap.size());
    }
}