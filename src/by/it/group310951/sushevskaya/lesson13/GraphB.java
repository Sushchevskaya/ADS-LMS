package by.it.group310951.sushevskaya.lesson13;

import java.util.*;

/**
 * Программа для обнаружения циклов в ориентированном графе.
 * Граф представляется в виде списка смежности, где вершины - целые числа.
 */
public class GraphB {

    public static void main(String[] args) {
        // Пример ввода: "1 -> 2, 2 -> 3, 3 -> 1" (граф с циклом)
        System.out.println("Введите ребра графа в формате '1 -> 2, 2 -> 3':");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Строим граф на основе ввода пользователя
        Map<Integer, List<Integer>> graph = buildGraph(input);

        // Проверяем наличие циклов и выводим результат
        if (hasCycle(graph)) {
            System.out.println("yes - граф содержит цикл");
        } else {
            System.out.println("no - граф не содержит циклов");
        }

        // Дополнительная информация о графе
        printGraphInfo(graph);
    }

    /**
     * Строит граф на основе входной строки.
     *
     * @param input строка с ребрами в формате "1 -> 2, 2 -> 3"
     * @return граф в виде списка смежности
     */
    private static Map<Integer, List<Integer>> buildGraph(String input) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        if (input.isEmpty()) {
            return graph;
        }

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);

            // Добавляем ребро a -> b в граф
            graph.putIfAbsent(a, new ArrayList<>());
            graph.get(a).add(b);

            // Для вершины b создаем пустой список смежности, если ее еще нет в графе
            graph.putIfAbsent(b, new ArrayList<>());
        }

        return graph;
    }

    /**
     * Проверяет наличие циклов в графе с помощью DFS.
     *
     * @param graph граф в виде списка смежности
     * @return true, если граф содержит цикл, иначе false
     */
    public static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        Set<Integer> visited = new HashSet<>();     // Посещенные вершины
        Set<Integer> recursionStack = new HashSet<>();  // Вершины в текущем пути DFS

        // Проверяем каждую вершину графа
        for (Integer node : graph.keySet()) {
            if (dfsDetectCycle(graph, node, visited, recursionStack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Вспомогательный метод для обнаружения циклов с помощью DFS.
     *
     * @param graph         граф в виде списка смежности
     * @param node          текущая вершина
     * @param visited       множество посещенных вершин
     * @param recursionStack множество вершин в текущем пути обхода
     * @return true, если обнаружен цикл
     */
    private static boolean dfsDetectCycle(Map<Integer, List<Integer>> graph, int node,
                                          Set<Integer> visited, Set<Integer> recursionStack) {
        // Если вершина уже в текущем пути обхода - обнаружен цикл
        if (recursionStack.contains(node)) {
            return true;
        }

        // Если вершина уже посещена и проверена - пропускаем
        if (visited.contains(node)) {
            return false;
        }

        // Помечаем вершину как посещенную и добавляем в текущий путь
        visited.add(node);
        recursionStack.add(node);

        // Рекурсивно проверяем всех соседей
        for (int neighbor : graph.get(node)) {
            if (dfsDetectCycle(graph, neighbor, visited, recursionStack)) {
                return true;
            }
        }

        // Убираем вершину из текущего пути перед возвратом
        recursionStack.remove(node);
        return false;
    }

    /**
     * Выводит информацию о графе.
     *
     * @param graph граф для вывода информации
     */
    private static void printGraphInfo(Map<Integer, List<Integer>> graph) {
        System.out.println("\nИнформация о графе:");
        System.out.println("Количество вершин: " + graph.size());

        int edgeCount = 0;
        for (List<Integer> neighbors : graph.values()) {
            edgeCount += neighbors.size();
        }
        System.out.println("Количество ребер: " + edgeCount);

        System.out.println("Список смежности:");
        for (Map.Entry<Integer, List<Integer>> entry : graph.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}