package by.it.group310951.sushevskaya.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class MHS<E> implements Set<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node[] table;
    private int size;

    private static class Node {
        Object data;
        Node next;

        Node(Object data) {
            this.data = data;
            this.next = null;
        }
    }

    public MHS() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private int getIndex(Object obj) {
        return Math.abs(obj.hashCode()) % table.length;
    }

    @Override
    public boolean contains(Object obj) {
        int index = getIndex(obj);
        Node current = table[index];
        while (current != null) {
            if (Objects.equals(current.data, obj)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean add(E obj) {
        if (contains(obj)) {
            return false;
        }

        int index = getIndex(obj);
        Node newNode = new Node(obj);
        newNode.next = table[index];
        table[index] = newNode;

        size++;

        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    @Override
    public boolean remove(Object obj) {
        int index = getIndex(obj);
        Node current = table[index];
        Node previous = null;

        while (current != null) {
            if (Objects.equals(current.data, obj)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {
        Node[] newTable = new Node[table.length * 2];
        for (Node node : table) {
            while (node != null) {
                int newIndex = Math.abs(node.data.hashCode()) % newTable.length;
                Node next = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node node : table) {
            while (node != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(node.data);
                first = false;
                node = node.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Реализация всех остальных методов интерфейса Collection<E>

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E element : c) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            if (!c.contains(element)) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            if (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;
            private Node current = null;

            @Override
            public boolean hasNext() {
                while (current == null && index < table.length) {
                    current = table[index++];
                }
                return current != null;
            }

            @Override
            public E next() {
                E data = (E) current.data;
                current = current.next;
                return data;
            }

            @Override
            public void remove() {
                MHS.this.remove(current.data);
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (Node node : table) {
            while (node != null) {
                array[i++] = node.data;
                node = node.next;
            }
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        int i = 0;
        for (Node node : table) {
            while (node != null) {
                a[i++] = (T) node.data;
                node = node.next;
            }
        }
        return a;
    }
}