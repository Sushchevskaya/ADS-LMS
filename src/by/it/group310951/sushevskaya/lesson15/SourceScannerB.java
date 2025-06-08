package by.it.group310951.sushevskaya.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Программа для сканирования исходного кода Java.
 * Находит все Java-файлы в проекте, исключая тестовые файлы.
 */

public class SourceScannerB {

    /**
     * Главный метод программы.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        System.out.println("=== Java Source Scanner ===");
        System.out.println("Находит все .java файлы, исключая тестовые\n");

        // Определяем базовый каталог для сканирования
        Path srcPath = resolveSrcPath();
        if (srcPath == null) {
            System.err.println("Ошибка: каталог 'src' не найден в текущей директории.");
            System.err.println("Поместите программу в корень проекта или в каталог src.");
            return;
        }

        System.out.println("Сканируем каталог: " + srcPath.toAbsolutePath());
        System.out.println("Критерии отбора:");
        System.out.println("  - Файлы с расширением .java");
        System.out.println("  - Без тестовых аннотаций (@Test, org.junit.Test)");
        System.out.println("\nРезультаты:");

        // 2. Выполняем сканирование и обработку файлов
        try (Stream<Path> files = Files.walk(srcPath)) {
            long count = files
                    .filter(SourceScannerB::isJavaSource)
                    .filter(SourceScannerB::notATestFile)
                    .peek(path -> System.out.println("  " + srcPath.relativize(path)))
                    .count();

            System.out.println("\nНайдено файлов: " + count);
            System.out.println("Сканирование завершено.");

        } catch (IOException e) {
            System.err.println("\nОшибка при сканировании:");
            System.err.println("  " + e.getMessage());
            System.err.println("Проверьте права доступа к файлам.");
        }
    }

    /**
     * Проверяет, является ли файл Java-исходником.
     *
     * @param path путь к файлу
     * @return true если файл имеет расширение .java
     */
    private static boolean isJavaSource(Path path) {
        return path.toString().endsWith(".java");
    }

    /**
     * Проверяет, что файл не является тестовым.
     *
     * @param file путь к файлу
     * @return true если файл не содержит тестовых аннотаций
     */
    private static boolean notATestFile(Path file) {
        try {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            return !(content.contains("@Test") ||
                    content.contains("org.junit.Test") ||
                    content.contains("@org.junit.jupiter.api.Test"));
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл: " + file);
            return false;
        }
    }

    /**
     * Определяет путь к каталогу src.
     *
     * @return Path к каталогу src или null если не найден
     */
    private static Path resolveSrcPath() {
        Path current = Path.of(System.getProperty("user.dir"));

        // Если уже в каталоге src
        if (current.getFileName().toString().equals("src")) {
            return current;
        }

        // Ищем src в подкаталогах
        Path srcPath = current.resolve("src");
        return Files.isDirectory(srcPath) ? srcPath : null;
    }
}
