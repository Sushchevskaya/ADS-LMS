package by.it.group310951.sushevskaya.lesson14;

import java.util.*;

/**
 * Программа для группировки связанных сайтов.
 * Использует структуру данных DSU (Disjoint Set Union) для эффективного объединения связанных сайтов.
 */
public class SitesB {

    /**
     * Класс для реализации структуры данных DSU (Disjoint Set Union) с использованием строковых ключей.
     * Позволяет эффективно объединять множества сайтов и находить их корни.
     */
    static class DSU {
        Map<String, String> parent = new HashMap<>();  // Хранит родительские сайты
        Map<String, Integer> size = new HashMap<>();   // Хранит размеры компонент связности

        /**
         * Находит корень множества для сайта x с применением пути сжатия.
         * @param x сайт, для которого ищем корень
         * @return корень множества, содержащего x
         */
        String find(String x) {
            // Если сайт встречается впервые, добавляем его как отдельное множество
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                size.put(x, 1);
            }
            // Применяем сжатие пути для оптимизации
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        /**
         * Объединяет два множества, содержащие сайты x и y.
         * @param x первый сайт
         * @param y второй сайт
         */
        void union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);
            if (!rootX.equals(rootY)) {
                // Объединяем меньшее множество с большим (оптимизация по размеру)
                if (size.get(rootX) < size.get(rootY)) {
                    String temp = rootX;
                    rootX = rootY;
                    rootY = temp;
                }
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }
    }

    /**
     * Главный метод программы.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU();

        System.out.println("Введите пары связанных сайтов в формате 'site1+site2'");
        System.out.println("Для завершения ввода введите 'end'");
        System.out.println("Пример ввода:");
        System.out.println("google.com+youtube.com");
        System.out.println("youtube.com+gmail.com");
        System.out.println("microsoft.com+linkedin.com");


        // Чтение входных данных
        int pairCount = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Проверка на завершение ввода
            if (line.equalsIgnoreCase("end")) {
                break;
            }

            // Парсинг пары сайтов
            String[] sites = line.split("\\+");
            if (sites.length != 2) {
                System.out.println("Ошибка: неверный формат ввода. Используйте 'site1+site2'");
                continue;
            }

            String site1 = sites[0].trim();
            String site2 = sites[1].trim();

            if (site1.isEmpty() || site2.isEmpty()) {
                System.out.println("Ошибка: названия сайтов не могут быть пустыми");
                continue;
            }

            // Объединение сайтов в одну группу
            dsu.union(site1, site2);
            pairCount++;
        }

        // Анализ результатов
        Map<String, Integer> groups = new HashMap<>();
        Set<String> allSites = new HashSet<>(dsu.parent.keySet());

        // Подсчет размеров групп
        for (String site : allSites) {
            String root = dsu.find(site);
            groups.put(root, groups.getOrDefault(root, 0) + 1);
        }

        // Сортировка групп по убыванию размера
        List<Integer> result = new ArrayList<>(groups.values());
        result.sort(Collections.reverseOrder());

        // Вывод результатов
        System.out.println("\nРезультаты анализа связей между сайтами:");
        System.out.println("Всего сайтов: " + allSites.size());
        System.out.println("Всего связей: " + pairCount);
        System.out.println("Количество групп: " + result.size());
        System.out.print("Размеры групп (по убыванию): ");

        for (int size : result) {
            System.out.print(size + " ");
        }

        // Дополнительная информация
        System.out.println("\n\nПримеры групп:");
        int examplesToShow = Math.min(3, result.size());
        for (int i = 0; i < examplesToShow; i++) {
            System.out.println("Группа " + (i+1) + ": " + result.get(i) + " сайтов");
        }

        scanner.close();
    }
}
