package by.it.group310951.sushevskaya.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

/**
 * Реализация отображения (map) на основе AVL-дерева.
 * Гарантирует логарифмическое время выполнения основных операций.
 */
public class MyAvlMap implements Map<Integer, String> {

    /**
     * Узел AVL-дерева, содержащий ключ, значение и информацию о балансе.
     */
    private static class Node {
        Integer key;         // Ключ узла
        String value;        // Значение узла
        Node left, right;    // Левый и правый потомки
        int height;          // Высота поддерева с корнем в этом узле

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1; // Высота нового узла всегда 1
        }
    }

    private Node root; // Корень дерева
    private int size;  // Количество элементов в дереве

    /**
     * Конструктор по умолчанию. Создает пустое дерево.
     */
    public MyAvlMap() {
        root = null;
        size = 0;
    }

    /**
     * Возвращает строковое представление отображения.
     * @return строка в формате {ключ1=значение1, ключ2=значение2, ...}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderTraversal(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2); // Удаляем последнюю запятую и пробел
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Добавляет пару ключ-значение или обновляет существующее значение.
     * @param key ключ
     * @param value значение
     * @return предыдущее значение или null, если ключ отсутствовал
     * @throws NullPointerException если ключ равен null
     */
    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException("Ключ не может быть null");
        Node[] result = new Node[1]; // Для хранения найденного узла
        root = insert(root, key, value, result);
        if (result[0] == null) {
            size++;
            return null;
        } else {
            String oldValue = result[0].value;
            result[0].value = value;
            return oldValue;
        }
    }

    /**
     * Удаляет элемент по ключу.
     * @param key ключ для удаления
     * @return удаленное значение или null, если ключ отсутствовал
     */
    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node[] result = new Node[1];
        root = delete(root, (Integer) key, result);
        if (result[0] != null) {
            size--;
            return result[0].value;
        }
        return null;
    }

    /**
     * Возвращает значение по ключу.
     * @param key ключ для поиска
     * @return значение или null, если ключ отсутствует
     */
    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node node = getNode(root, (Integer) key);
        return node == null ? null : node.value;
    }

    /**
     * Проверяет наличие ключа в отображении.
     * @param key ключ для проверки
     * @return true, если ключ присутствует
     */
    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return getNode(root, (Integer) key) != null;
    }

    /**
     * Возвращает количество элементов в отображении.
     * @return размер отображения
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Очищает отображение, удаляя все элементы.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Проверяет, пусто ли отображение.
     * @return true, если отображение пусто
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // Вспомогательные методы для работы с AVL-деревом

    /**
     * Обход дерева в порядке возрастания ключей.
     * @param node текущий узел
     * @param sb строковый буфер для накопления результата
     */
    private void inOrderTraversal(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderTraversal(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderTraversal(node.right, sb);
        }
    }

    /**
     * Вставляет новый узел или обновляет существующий.
     * @param node текущий узел
     * @param key ключ для вставки
     * @param value значение для вставки
     * @param result массив для хранения найденного узла
     * @return сбалансированный узел
     */
    private Node insert(Node node, Integer key, String value, Node[] result) {
        if (node == null) return new Node(key, value);

        if (key < node.key) {
            node.left = insert(node.left, key, value, result);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value, result);
        } else {
            result[0] = node; // Ключ уже существует
            return node;
        }

        return balance(node);
    }

    /**
     * Удаляет узел по ключу.
     * @param node текущий узел
     * @param key ключ для удаления
     * @param result массив для хранения удаленного узла
     * @return сбалансированный узел
     */
    private Node delete(Node node, Integer key, Node[] result) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = delete(node.left, key, result);
        } else if (key > node.key) {
            node.right = delete(node.right, key, result);
        } else {
            result[0] = node; // Узел найден
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Узел с двумя потомками - находим минимальный в правом поддереве
            Node min = getMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, min.key, new Node[1]);
        }

        return balance(node);
    }

    /**
     * Находит узел по ключу.
     * @param node текущий узел
     * @param key ключ для поиска
     * @return найденный узел или null
     */
    private Node getNode(Node node, Integer key) {
        while (node != null) {
            if (key < node.key) {
                node = node.left;
            } else if (key > node.key) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    /**
     * Находит узел с минимальным ключом в поддереве.
     * @param node корень поддерева
     * @return узел с минимальным ключом
     */
    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Балансирует узел.
     * @param node узел для балансировки
     * @return сбалансированный узел
     */
    private Node balance(Node node) {
        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);

        // Левое поддерево слишком высокое
        if (balanceFactor > 1) {
            if (getBalanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        // Правое поддерево слишком высокое
        if (balanceFactor < -1) {
            if (getBalanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Обновляет высоту узла.
     * @param node узел для обновления
     */
    private void updateHeight(Node node) {
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    /**
     * Возвращает высоту узла.
     * @param node узел
     * @return высота узла (0 для null)
     */
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Вычисляет фактор баланса узла.
     * @param node узел
     * @return разница высот левого и правого поддеревьев
     */
    private int getBalanceFactor(Node node) {
        return getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Выполняет левый поворот.
     * @param node узел для поворота
     * @return новый корень поддерева
     */
    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    /**
     * Выполняет правый поворот.
     * @param node узел для поворота
     * @return новый корень поддерева
     */
    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    // Не реализованные методы
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
    @Override public String getOrDefault(Object key, String defaultValue) { throw new UnsupportedOperationException(); }

    /**
     * Тестовый метод для демонстрации работы MyAvlMap.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("=== Тестирование MyAvlMap ===");

        // Создаем отображение
        MyAvlMap map = new MyAvlMap();

        // Добавляем элементы
        System.out.println("\nДобавляем элементы:");
        System.out.println("put(5, \"Five\"): " + map.put(5, "Five"));
        System.out.println("put(3, \"Three\"): " + map.put(3, "Three"));
        System.out.println("put(7, \"Seven\"): " + map.put(7, "Seven"));
        System.out.println("put(5, \"New Five\"): " + map.put(5, "New Five")); // Обновление
        System.out.println("Отображение: " + map);
        System.out.println("Размер: " + map.size());

        // Проверяем наличие ключей
        System.out.println("\nПроверяем наличие ключей:");
        System.out.println("containsKey(3): " + map.containsKey(3));
        System.out.println("containsKey(10): " + map.containsKey(10));

        // Получаем значения
        System.out.println("\nПолучаем значения:");
        System.out.println("get(5): " + map.get(5));
        System.out.println("get(2): " + map.get(2));

        // Удаляем элементы
        System.out.println("\nУдаляем элементы:");
        System.out.println("remove(3): " + map.remove(3));
        System.out.println("remove(10): " + map.remove(10)); // Несуществующий
        System.out.println("Отображение после удаления: " + map);
        System.out.println("Размер: " + map.size());

        // Проверяем балансировку
        System.out.println("\nПроверяем балансировку:");
        map.put(1, "One");
        map.put(2, "Two");
        map.put(4, "Four");
        map.put(6, "Six");
        map.put(8, "Eight");
        System.out.println("Отображение после добавления элементов: " + map);
        System.out.println("Размер: " + map.size());

        // Очищаем отображение
        System.out.println("\nОчищаем отображение:");
        map.clear();
        System.out.println("Отображение после очистки: " + map);
        System.out.println("Размер: " + map.size());
        System.out.println("isEmpty(): " + map.isEmpty());

        System.out.println("\n=== Тестирование завершено ===");
    }
}