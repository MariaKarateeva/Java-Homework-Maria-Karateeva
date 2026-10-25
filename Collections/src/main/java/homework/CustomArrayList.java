package homework;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Свой ArrayList. Элементы лежат в обычном массиве,
 * а когда он заполняется, мы увеличиваем его в 1.5 раза.
 */
public class CustomArrayList<E> implements CustomList<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    /**
     * Создаёт пустой список, в массиве сразу есть место на 10 элементов.
     */
    public CustomArrayList() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Добавляет элемент в конец списка. Если передать null, будет NullPointerException.
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("Нельзя добавить null");
        }
        if (size == data.length) {
            grow();
        }
        data[size] = element;
        size++;
    }

    /**
     * Возвращает элемент по индексу. Если индекс неправильный, будет IndexOutOfBoundsException.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) data[index];
    }

    /**
     * Удаляет элемент по индексу и возвращает его. Остальные элементы сдвигаются влево,
     * а освободившаяся ячейка становится null.
     * Если индекс неправильный, будет IndexOutOfBoundsException.
     */
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E removed = (E) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        return removed;
    }

    /**
     * Возвращает количество элементов в списке.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, пустой ли список.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Возвращает итератор, который идёт по списку с начала до конца.
     * Удалять элементы через итератор нельзя.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = (E) data[current];
                current++;
                return element;
            }
        };
    }

    private void grow() {
        int newCapacity = data.length * 3 / 2;
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
        }
    }
}