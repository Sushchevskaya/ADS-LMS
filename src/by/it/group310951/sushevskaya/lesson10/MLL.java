package by.it.group310951.sushevskaya.lesson10;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MLL<E> implements Deque<E> {

    private class Node {
        E data;
        Node prev;
        Node next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    // Реализация метода add, который добавляет элемент в конец
    @Override
    public boolean add(E element) {
        addLast(element);  // Эквивалентно addLast для двусвязного списка
        return true;
    }

    // Добавить в начало
    @Override
    public void addFirst(E element) {
        Node node = new Node(element);
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = node;
        size++;
    }

    // Добавить в конец
    @Override
    public void addLast(E element) {
        Node node = new Node(element);
        node.prev = tail;
        if (tail != null) tail.next = node;
        tail = node;
        if (head == null) head = node;
        size++;
    }

    // Удалить по индексу
    public E remove(int index) {
        checkIndex(index);
        Node current = getNode(index);
        return unlink(current);
    }

    // Удалить по объекту
    @Override
    public boolean remove(Object o) {
        Node current = head;
        while (current != null) {
            if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
                unlink(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Получить первый элемент
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E getFirst() {
        if (head == null) throw new NoSuchElementException();
        return head.data;
    }

    @Override
    public E getLast() {
        if (tail == null) throw new NoSuchElementException();
        return tail.data;
    }

    // Извлечь и удалить первый
    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E pollFirst() {
        if (head == null) return null;
        return unlink(head);
    }

    @Override
    public E pollLast() {
        if (tail == null) return null;
        return unlink(tail);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E removeFirst() {
        if (head == null) throw new NoSuchElementException();
        return unlink(head);
    }

    @Override
    public E removeLast() {
        if (tail == null) throw new NoSuchElementException();
        return unlink(tail);
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public int size() {
        return size;
    }

    private Node getNode(int index) {
        Node current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) current = current.next;
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private E unlink(Node node) {
        E data = node.data;
        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) prev.next = next;
        else head = next;

        if (next != null) next.prev = prev;
        else tail = prev;

        size--;
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // Методы-заглушки для полноты реализации интерфейса Deque

    @Override public boolean offerFirst(E e) { addFirst(e); return true; }
    @Override public boolean offerLast(E e) { addLast(e); return true; }
    @Override public E peekFirst() { return (head != null) ? head.data : null; }
    @Override public E peekLast() { return (tail != null) ? tail.data : null; }
    @Override public boolean removeFirstOccurrence(Object o) { return remove(o); }
    @Override public boolean removeLastOccurrence(Object o) { return remove(o); }
    @Override public boolean offer(E e) { return offerLast(e); }
    @Override public E peek() { return peekFirst(); }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { while (head != null) unlink(head); }

    // Не реализованы: итераторы и массовые операции
    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException(); }
    @Override public Iterator<E> descendingIterator() { throw new UnsupportedOperationException(); }
    @Override public boolean contains(Object o) { throw new UnsupportedOperationException(); }
    @Override public Object[] toArray() { throw new UnsupportedOperationException(); }
    @Override public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
    @Override public boolean containsAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean addAll(java.util.Collection<? extends E> c) { throw new UnsupportedOperationException(); }
    @Override public boolean removeAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
    @Override public boolean retainAll(java.util.Collection<?> c) { throw new UnsupportedOperationException(); }
}