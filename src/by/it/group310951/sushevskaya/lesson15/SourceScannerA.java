package by.it.group310951.sushevskaya.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Утилита для сканирования исходного кода Java.
 * Находит все Java-файлы в каталоге src и его подкаталогах,
 * исключая файлы с тестовыми аннотациями.
 */

public class SourceScannerA {

    /**
     * Главный метод программы. Выполняет поиск Java-файлов без тестовых аннотаций.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("=== Сканер исходного кода Java ===");
        System.out.println("Поиск Java-файлов без тестовых аннотаций в каталоге src\n");

        // 1. Определение целевого каталога
        Path srcDir = determineSourceDirectory();
        if (srcDir == null) {
            return;
        }

        // 2. Вывод информации о сканировании
        printScanInfo(srcDir);

        // 3. Выполнение сканирования и обработка результатов
        scanAndProcessFiles(srcDir);
    }

    /**
     * Определяет каталог с исходным кодом для сканирования.
     *
     * @return Path каталога src или null, если каталог не найден
     */

    private static Path determineSourceDirectory() {
        Path currentDir = Path.of(System.getProperty("user.dir"));

        // Проверяем, находится ли программа непосредственно в каталоге src
        if ("src".equals(currentDir.getFileName().toString())) {
            return currentDir;
        }

        // Иначе проверяем наличие подкаталога src
        Path srcDir = currentDir.resolve("src");
        if (!Files.isDirectory(srcDir)) {
            System.err.println("\nОшибка: Каталог 'src' не найден в: " + currentDir);
            System.err.println("Поместите программу в корень проекта или в каталог src");
            return null;
        }

        return srcDir;
    }

    /**
     * Выводит информацию о параметрах сканирования.
     *
     * @param srcDir каталог для сканирования
     */
    private static void printScanInfo(Path srcDir) {
        System.out.println("Каталог для сканирования: " + srcDir.toAbsolutePath());
        System.out.println("Критерии отбора:");
        System.out.println("  - Файлы с расширением .java");
        System.out.println("  - Без аннотаций @Test или org.junit.Test");
        System.out.println("\nРезультаты сканирования:");
    }

    /**
     * Выполняет сканирование файлов и обработку результатов.
     *
     * @param srcDir каталог для сканирования
     */
    private static void scanAndProcessFiles(Path srcDir) {
        try (Stream<Path> paths = Files.walk(srcDir)) {
            long fileCount = paths
                    .filter(SourceScannerA::isValidJavaFile)
                    .peek(path -> System.out.println("  Найден: " + srcDir.relativize(path)))
                    .count();

            System.out.println("\nСканирование завершено.");
            System.out.println("Всего найдено подходящих файлов: " + fileCount);

        } catch (IOException e) {
            System.err.println("\nОшибка при сканировании каталога:");
            System.err.println("  " + e.getMessage());
            System.err.println("Проверьте права доступа к файлам.");
        }
    }

    /**
     * Проверяет, является ли файл Java-файлом без тестовых аннотаций.
     *
     * @param path путь к файлу
     * @return true если файл соответствует критериям
     */
    private static boolean isValidJavaFile(Path path) {
        // Проверка расширения файла
        if (!path.toString().endsWith(".java")) {
            return false;
        }

        try {
            String content = Files.readString(path, StandardCharsets.UTF_8);

            // Проверка на наличие тестовых аннотаций
            boolean hasTestAnnotations = content.contains("@Test") ||
                    content.contains("org.junit.Test");

            // Дополнительная проверка для популярных тестовых фреймворков
            boolean hasOtherTestMarkers = content.contains("@org.junit.jupiter.api.Test") ||
                    content.contains("@Testable") ||
                    content.contains("extends TestCase");

            return !(hasTestAnnotations || hasOtherTestMarkers);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + path);
            return false;
        }
    }
}
