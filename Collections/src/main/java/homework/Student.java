package homework;

import java.util.Objects;

/**
 * Студент. После создания его данные менять нельзя.
 */
public final class Student {

    private final int id;
    private final String name;
    private final double grade;

    /**
     * Создаёт студента. id должен быть больше 0, имя не пустое, оценка от 0 до 10.
     * Если что-то не так, будет IllegalArgumentException.
     */
    public Student(int id, String name, double grade) {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не должно быть пустым");
        }
        if (grade < 0.0 || grade > 10.0) {
            throw new IllegalArgumentException("Оценка должна быть от 0.0 до 10.0");
        }
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    /**
     * Возвращает id студента.
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает имя студента.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает оценку студента.
     */
    public double getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student other = (Student) o;
        return id == other.id
                && Double.compare(grade, other.grade) == 0
                && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, grade);
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name + ", grade=" + grade + "}";
    }
}