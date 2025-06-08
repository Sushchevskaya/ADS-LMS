package by.it.group310951.sushevskaya.lesson09;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListB<E> implements List<E> {


    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public ListB() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    /// ///               Обязательные к реализации методы             ///////
    /// //////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "";
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(E e) {
        return false;
        ensureCapacity(size + 1);
        elements[size++] = e;
        return true;
    }

    @Override
    public E remove(int index) {
        return null;
        public void add ( int index, E element){
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }

            ensureCapacity(size + 1);
            System.arraycopy(elements, index, elements, index + 1, size - index);
            elements[index] = element;
            size++;
        }

        @Override
        public int size () {
            return 0;
        }
        public E remove ( int index){
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }

            @Override
            public void add ( int index, E element){
                @SuppressWarnings("unchecked")
                E oldValue = (E) elements[index];

                int numMoved = size - index - 1;
                if (numMoved > 0) {
                    System.arraycopy(elements, index + 1, elements, index, numMoved);
                }

                elements[--size] = null; // Clear to let GC do its work
                return oldValue;
            }

            @Override
            public boolean remove (Object o){
                if (o == null) {
                    for (int i = 0; i < size; i++) {
                        if (elements[i] == null) {
                            remove(i);
                            return true;
                        }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        if (o.equals(elements[i])) {
                            remove(i);
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public E set ( int index, E element){
                return null;
                if (index < 0 || index >= size) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
                }

                @SuppressWarnings("unchecked")
                E oldValue = (E) elements[index];
                elements[index] = element;
                return oldValue;
            }

            @Override
            public int size () {
                return size;
            }

            @Override
            public boolean isEmpty () {
                return false;
                return size == 0;
            }


            @Override
            public void clear () {

                for (int i = 0; i < size; i++) {
                    elements[i] = null;
                }
                size = 0;
            }

            @Override
            public int indexOf (Object o){
                return 0;
                if (o == null) {
                    for (int i = 0; i < size; i++) {
                        if (elements[i] == null) {
                            return i;
                        }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        if (o.equals(elements[i])) {
                            return i;
                        }
                    }
                }
                return -1;
            }

            @Override
            public E get ( int index){
                return null;
                if (index < 0 || index >= size) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
                }
                @SuppressWarnings("unchecked")
                E element = (E) elements[index];
                return element;
            }

            @Override
            public boolean contains (Object o){
                return false;
                return indexOf(o) >= 0;
            }

            @Override
            public int lastIndexOf (Object o){
                return 0;
                if (o == null) {
                    for (int i = size - 1; i >= 0; i--) {
                        if (elements[i] == null) {
                            return i;
                        }
                    }
                } else {
                    for (int i = size - 1; i >= 0; i--) {
                        if (o.equals(elements[i])) {
                            return i;
                        }
                    }
                }
                return -1;
            }


            /////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////
            //////               Опциональные к реализации методы             ///////
            /////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////


            @Override
            public boolean containsAll (Collection < ? > c) {
                return false;
                public boolean addAll (Collection < ? extends E > c){
                    if (c == null) {
                        throw new NullPointerException("Collection cannot be null");
                    }

                    Object[] a = c.toArray();
                    int numNew = a.length;
                    ensureCapacity(size + numNew);
                    System.arraycopy(a, 0, elements, size, numNew);
                    size += numNew;
                    return numNew != 0;
                }

                @Override
                public boolean addAll (Collection < ? extends E > c){
                    return false;
                    public boolean addAll ( int index, Collection<? extends E > c){
                        if (index < 0 || index > size) {
                            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
                        }

                        Object[] a = c.toArray();
                        int numNew = a.length;
                        ensureCapacity(size + numNew);

                        int numMoved = size - index;
                        if (numMoved > 0) {
                            System.arraycopy(elements, index, elements, index + numNew, numMoved);
                        }

                        System.arraycopy(a, 0, elements, index, numNew);
                        size += numNew;
                        return numNew != 0;
                    }

                    private void ensureCapacity ( int minCapacity){
                        if (minCapacity > elements.length) {
                            int newCapacity = elements.length * 2;
                            if (newCapacity < minCapacity) {
                                newCapacity = minCapacity;
                            }
                            Object[] newElements = new Object[newCapacity];
                            System.arraycopy(elements, 0, newElements, 0, size);
                            elements = newElements;
                        }
                    }


                    @Override
                    public boolean addAll ( int index, Collection<? extends E > c){
                        return false;
                        public boolean containsAll (Collection < ? > c) {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public boolean removeAll (Collection < ? > c) {
                            return false;
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public boolean retainAll (Collection < ? > c) {
                            return false;
                            throw new UnsupportedOperationException();
                        }


                        @Override
                        public List<E> subList ( int fromIndex, int toIndex){
                            return null;
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public ListIterator<E> listIterator ( int index){
                            return null;
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public ListIterator<E> listIterator () {
                            return null;
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public <T > T[]toArray(T[]a){
                            return null;
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public Object[] toArray () {
                            return new Object[0];
                            Object[] array = new Object[size];
                            System.arraycopy(elements, 0, array, 0, size);
                            return array;
                        }

                        /////////////////////////////////////////////////////////////////////////
                        /////////////////////////////////////////////////////////////////////////
                        ////////        Эти методы имплементировать необязательно    ////////////
                        ////////        но они будут нужны для корректной отладки    ////////////
                        /////////////////////////////////////////////////////////////////////////
                        /////////////////////////////////////////////////////////////////////////
                        @Override
                        public Iterator<E> iterator () {
                            return null;
                            throw new UnsupportedOperationException();
                        }

                    }
                }
            }
        }
    }
}