package homework;

/**
 * Свой интерфейс списка. Можно добавлять, получать и удалять элементы по индексу.
 */
public interface CustomList<E> extends Iterable<E> {

    /**
     * Добавляет элемент в конец списка. Если передать null, будет NullPointerException.
     */
    void add(E element);

    /**
     * Возвращает элемент по индексу. Если индекс неправильный, будет IndexOutOfBoundsException.
     */
    E get(int index);

    /**
     * Удаляет элемент по индексу и возвращает его. Остальные элементы сдвигаются влево.
     * Если индекс неправильный, будет IndexOutOfBoundsException.
     */
    E remove(int index);

    /**
     * Возвращает количество элементов в списке.
     */
    int size();

    /**
     * Проверяет, пустой ли список.
     */
    boolean isEmpty();
}