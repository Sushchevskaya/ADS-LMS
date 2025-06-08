package by.it.group310951.sushevskaya.lesson14;

import java.util.*;

/**
 * Программа для группировки точек в 3D пространстве на основе заданного расстояния.
 * Использует структуру данных DSU (Disjoint Set Union) для эффективного объединения точек.
 */
public class PointsA {

    /**
     * Класс для реализации структуры данных DSU (Disjoint Set Union).
     * Позволяет эффективно объединять множества и находить их корни.
     */
    static class DSU {
        int[] parent;  // Массив для хранения родительских элементов
        int[] size;    // Массив для хранения размеров множеств

        /**
         * Конструктор инициализирует DSU для n элементов.
         * @param n количество элементов
         */
        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;  // Каждый элемент изначально является своим родителем
                size[i] = 1;    // Размер каждого множества изначально 1
            }
        }

        /**
         * Находит корень множества для элемента x с применением пути сжатия.
         * @param x элемент, для которого ищем корень
         * @return корень множества, содержащего x
         */
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // Рекурсивное сжатие пути
            }
            return parent[x];
        }

        /**
         * Объединяет два множества, содержащие x и y.
         * @param x первый элемент
         * @param y второй элемент
         */
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                // Объединяем меньшее множество с большим
                if (size[rootX] < size[rootY]) {
                    int temp = rootX;
                    rootX = rootY;
                    rootY = temp;
                }
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }

    /**
     * Главный метод программы.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите максимальное расстояние для объединения точек:");
        int distance = scanner.nextInt();  // Максимальное расстояние для объединения

        System.out.println("Введите количество точек:");
        int n = scanner.nextInt();  // Количество точек

        System.out.println("Введите координаты (x y z) и радиус для каждой точки:");
        int[][] points = new int[n][3];  // Массив для хранения координат и радиуса каждой точки

        // Считывание координат и радиуса для каждой точки
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();  // x координата
            points[i][1] = scanner.nextInt();  // y координата
            points[i][2] = scanner.nextInt();  // z координата (радиус)
        }

        // Создаём структуру данных DSU для объединения точек
        DSU dsu = new DSU(n);

        // Сравниваем расстояние между каждой парой точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисление евклидова расстояния между точками в 3D пространстве
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];
                double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);

                // Если расстояние меньше заданного, объединяем точки
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // Создание мапы для подсчета размера каждого множества
        Map<Integer, Integer> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);  // Находим корень множества для точки
            groups.put(root, groups.getOrDefault(root, 0) + 1);  // Увеличиваем счетчик для этого множества
        }

        // Собираем размеры множеств в список и сортируем по убыванию
        List<Integer> result = new ArrayList<>(groups.values());
        result.sort(Collections.reverseOrder());

        // Выводим результаты
        System.out.println("\nРезультаты группировки точек:");
        System.out.println("Количество групп: " + result.size());
        System.out.print("Размеры групп (по убыванию): ");
        for (int size : result) {
            System.out.print(size + " ");
        }

        // Дополнительная информация
        System.out.println("\n\nДополнительная информация:");
        System.out.println("Всего точек: " + n);
        System.out.println("Максимальное расстояние для объединения: " + distance);

        scanner.close();
    }
}