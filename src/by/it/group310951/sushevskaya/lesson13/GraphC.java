package by.it.group310951.sushevskaya.lesson13;

import java.util.*;

/**
 * Программа для нахождения компонент сильной связности (SCC) в ориентированном графе.
 * Использует алгоритм Косарайю, состоящий из двух проходов DFS.
 */
public class GraphC {

    // Список смежности для исходного графа
    private List<List<Integer>> adjList;
    // Список смежности для транспонированного графа
    private List<List<Integer>> adjListTrans;
    // Отображение вершины (буквы) в индекс
    private Map<String, Integer> vertexToIndex;
    // Отображение индекса в вершину (букву)
    private Map<Integer, String> indexToVertex;
    // Стек для порядка завершения первого DFS
    private Stack<Integer> stack;
    // Множество посещённых вершин
    private Set<Integer> visited;

    /**
     * Конструктор инициализирует все необходимые структуры данных.
     */
    public GraphC() {
        adjList = new ArrayList<>();
        adjListTrans = new ArrayList<>();
        vertexToIndex = new HashMap<>();
        indexToVertex = new HashMap<>();
        stack = new Stack<>();
        visited = new HashSet<>();
    }

    /**
     * Добавляет вершину в граф, если она еще не существует.
     * @param v имя вершины (строка)
     * @return индекс вершины в списке смежности
     */
    private int addVertex(String v) {
        if (!vertexToIndex.containsKey(v)) {
            int index = vertexToIndex.size();
            vertexToIndex.put(v, index);
            indexToVertex.put(index, v);
            adjList.add(new ArrayList<>());
            adjListTrans.add(new ArrayList<>());
            return index;
        }
        return vertexToIndex.get(v);
    }

    /**
     * Добавляет ориентированное ребро в граф.
     * @param u начальная вершина ребра
     * @param v конечная вершина ребра
     */
    private void addEdge(String u, String v) {
        int uIndex = addVertex(u);
        int vIndex = addVertex(v);
        adjList.get(uIndex).add(vIndex);       // Добавляем в исходный граф
        adjListTrans.get(vIndex).add(uIndex);  // Добавляем в транспонированный граф
    }

    /**
     * Первый проход DFS для заполнения стека порядком завершения.
     * @param v текущая вершина
     */
    private void dfsFirst(int v) {
        visited.add(v);
        for (int neighbor : adjList.get(v)) {
            if (!visited.contains(neighbor)) {
                dfsFirst(neighbor);
            }
        }
        stack.push(v);  // Добавляем вершину в стек после обработки всех соседей
    }

    /**
     * Второй проход DFS для нахождения SCC в транспонированном графе.
     * @param v текущая вершина
     * @param component список для хранения текущей компоненты связности
     */
    private void dfsSecond(int v, List<Integer> component) {
        visited.add(v);
        component.add(v);
        for (int neighbor : adjListTrans.get(v)) {
            if (!visited.contains(neighbor)) {
                dfsSecond(neighbor, component);
            }
        }
    }

    /**
     * Находит все компоненты сильной связности в графе.
     * @return список SCC, каждая из которых отсортирована лексикографически
     */
    private List<List<String>> findSCC() {
        // Первый проход DFS для заполнения стека
        visited.clear();
        for (int v = 0; v < adjList.size(); v++) {
            if (!visited.contains(v)) {
                dfsFirst(v);
            }
        }

        // Второй проход DFS по транспонированному графу
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited.contains(v)) {
                List<Integer> component = new ArrayList<>();
                dfsSecond(v, component);

                // Преобразуем индексы в буквы и сортируем
                List<String> scc = new ArrayList<>();
                for (int index : component) {
                    scc.add(indexToVertex.get(index));
                }
                Collections.sort(scc);
                sccs.add(scc);
            }
        }

        return sccs;
    }

    /**
     * Главный метод программы.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Пример ввода: "A->B, B->C, C->A, B->D, D->E, E->F, F->D"
        System.out.println("Введите ребра графа в формате 'A->B, B->C':");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        GraphC graph = new GraphC();

        try {
            // Парсим входную строку
            if (!input.isEmpty()) {
                String[] edges = input.split(",\\s*");
                for (String edge : edges) {
                    String[] vertices = edge.split("->");
                    if (vertices.length != 2) {
                        throw new IllegalArgumentException("Неправильный формат ребра: " + edge);
                    }
                    String u = vertices[0].trim();
                    String v = vertices[1].trim();
                    graph.addEdge(u, v);
                }
            }

            // Находим и выводим компоненты сильной связности
            List<List<String>> sccs = graph.findSCC();

            System.out.println("\nКомпоненты сильной связности:");
            if (sccs.isEmpty()) {
                System.out.println("Граф не содержит вершин");
            } else {
                // Сортируем SCC по первой вершине в компоненте
                sccs.sort(Comparator.comparing(list -> list.get(0)));

                for (List<String> scc : sccs) {
                    System.out.print("Компонента: ");
                    for (String vertex : scc) {
                        System.out.print(vertex);
                    }
                    System.out.println();
                }
            }

            // Дополнительная информация о графе
            System.out.println("\nОбщая информация:");
            System.out.println("Количество вершин: " + graph.vertexToIndex.size());
            System.out.println("Количество ребер: " +
                    graph.adjList.stream().mapToInt(List::size).sum());
            System.out.println("Количество компонент сильной связности: " + sccs.size());

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.err.println("Правильный формат ввода: 'A->B, B->C, C->A'");
        }
    }
}