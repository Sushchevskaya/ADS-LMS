package by.it.group310951.sushevskaya.lesson11;

public class MLHS<E> implements java.util.Set<E> {

    private static class Node<E> {
        E value;
        Node<E> next;         // For hash collisions
        Node<E> prevOrder;    // For insertion order
        Node<E> nextOrder;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E>[] table;
    private int size;
    private int capacity = 16;
    private float loadFactor = 0.75f;
    private Node<E> headOrder;
    private Node<E> tailOrder;

    @SuppressWarnings("unchecked")
    public MLHS() {
        table = (Node<E>[]) new Node[capacity];
    }

    private int index(Object key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capacity;
    }

    @Override
    public boolean add(Object obj) {
        E value = (E) obj;
        int idx = index(value);
        Node<E> node = table[idx];

        while (node != null) {
            if ((node.value == null && value == null) || (node.value != null && node.value.equals(value))) {
                return false; // already exists
            }
            node = node.next;
        }

        Node<E> newNode = new Node<>(value);
        newNode.next = table[idx];
        table[idx] = newNode;

        if (tailOrder == null) {
            headOrder = tailOrder = newNode;
        } else {
            tailOrder.nextOrder = newNode;
            newNode.prevOrder = tailOrder;
            tailOrder = newNode;
        }

        size++;
        if (size > capacity * loadFactor) {
            resize();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        Node<E>[] oldTable = table;
        table = (Node<E>[]) new Node[capacity];
        Node<E> current = headOrder;

        while (current != null) {
            int idx = index(current.value);
            Node<E> nextInOrder = current.nextOrder;
            current.next = table[idx];
            table[idx] = current;
            current = nextInOrder;
        }
    }

    @Override
    public boolean contains(Object obj) {
        int idx = index(obj);
        Node<E> node = table[idx];
        while (node != null) {
            if ((node.value == null && obj == null) || (node.value != null && node.value.equals(obj))) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object obj) {
        int idx = index(obj);
        Node<E> node = table[idx];
        Node<E> prev = null;

        while (node != null) {
            if ((node.value == null && obj == null) || (node.value != null && node.value.equals(obj))) {
                if (prev == null) {
                    table[idx] = node.next;
                } else {
                    prev.next = node.next;
                }

                // Remove from insertion order
                if (node.prevOrder != null) {
                    node.prevOrder.nextOrder = node.nextOrder;
                } else {
                    headOrder = node.nextOrder;
                }
                if (node.nextOrder != null) {
                    node.nextOrder.prevOrder = node.prevOrder;
                } else {
                    tailOrder = node.prevOrder;
                }

                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    @Override
    public void clear() {
        table = (Node<E>[]) new Node[capacity];
        headOrder = tailOrder = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = headOrder;
        while (current != null) {
            sb.append(current.value);
            if (current.nextOrder != null) {
                sb.append(", ");
            }
            current = current.nextOrder;
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean containsAll(java.util.Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean removeAll(java.util.Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            changed |= remove(e);
        }
        return changed;
    }

    @Override
    public boolean retainAll(java.util.Collection<?> c) {
        boolean changed = false;
        Node<E> current = headOrder;
        while (current != null) {
            Node<E> next = current.nextOrder;
            if (!c.contains(current.value)) {
                remove(current.value);
                changed = true;
            }
            current = next;
        }
        return changed;
    }

    // Unsupported operations for Iterator and Spliterator
    @Override
    public java.util.Iterator<E> iterator() {
        throw new UnsupportedOperationException("Iterator not implemented");
    }

    @Override
    public java.util.Spliterator<E> spliterator() {
        throw new UnsupportedOperationException("Spliterator not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray not implemented");
    }
}