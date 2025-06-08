package by.it.group310951.sushevskaya.lesson13;

import java.util.*;

/**
 * Программа для выполнения топологической сортировки ориентированного графа.
 * Граф представляется в виде списка смежности, где вершины - строковые идентификаторы.
 */

public class GraphA {

    public static void main(String[] args) {
        // Пример ввода: "A -> B, B -> C, A -> D, D -> C"
        System.out.println("Введите ребра графа в формате 'A -> B, B -> C':");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Строим граф и считаем полустепени захода вершин
        Map<String, List<String>> graph = buildGraph(input);
        Map<String, Integer> inDegree = calculateInDegree(graph);

        System.out.println("\nРезультат топологической сортировки:");
        topologicalSort(graph, inDegree);
    }

    /**
     * Строит граф на основе входной строки.
     *
     * @param input строка с ребрами в формате "A -> B, B -> C"
     * @return граф в виде списка смежности
     */
    private static Map<String, List<String>> buildGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] vertices = edge.split(" -> ");
            String u = vertices[0];
            String v = vertices[1];

            // Добавляем ребро u -> v в граф
            graph.putIfAbsent(u, new ArrayList<>());
            graph.get(u).add(v);

            // Для вершины v создаем пустой список смежности, если ее еще нет в графе
            graph.putIfAbsent(v, new ArrayList<>());
        }

        return graph;
    }

    /**
     * Вычисляет полустепени захода для всех вершин графа.
     *
     * @param graph граф в виде списка смежности
     * @return словарь с полустепенями захода для каждой вершины
     */
    private static Map<String, Integer> calculateInDegree(Map<String, List<String>> graph) {
        Map<String, Integer> inDegree = new HashMap<>();

        // Инициализируем счетчики для всех вершин нулями
        for (String node : graph.keySet()) {
            inDegree.put(node, 0);
        }

        // Увеличиваем счетчики для вершин, в которые ведут ребра
        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        return inDegree;
    }

    /**
     * Выполняет топологическую сортировку графа с использованием алгоритма Кана.
     *
     * @param graph    граф в виде списка смежности
     * @param inDegree словарь с полустепенями захода вершин
     */
    public static void topologicalSort(Map<String, List<String>> graph, Map<String, Integer> inDegree) {

        // Используем PriorityQueue для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем в очередь все вершины с нулевой полустепенью захода
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем полустепени захода для всех соседей
            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);

                // Если полустепень захода стала нулевой, добавляем в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Проверяем наличие цикла в графе
        if (result.size() != graph.size()) {
            System.out.println("Граф содержит циклы, топологическая сортировка невозможна!");
            return;
        }

        // Выводим результат
        for (String node : result) {
            System.out.print(node + " ");
        }
    }
}