import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class Loganalyzer {
    public static void main(String[] args) {
        Path folder = Paths.get("logs");
        String keyword = "ERROR";

        int totalLines = 0;
        int totalKeywordLines = 0;

        try {
            Files.createDirectories(folder);

            Files.write(folder.resolve("a.txt"), Arrays.asList(
                "INFO Server started",
                "ERROR Database failed",
                "INFO User logged in"
            ));
            Files.write(folder.resolve("b.txt"), Arrays.asList(
                "INFO Request received",
                "ERROR File not found",
                "INFO Request completed",
                "ERROR Server stopped"
            ));
            Files.write(folder.resolve("c.txt"), Arrays.asList(
                "INFO System running",
                "INFO Backup completed"
            ));

            try (DirectoryStream<Path> files = Files.newDirectoryStream(folder, "*.txt")) {
                for (Path file : files) {
                    int lines = 0;
                    int keywordLines = 0;

                    try (BufferedReader reader = Files.newBufferedReader(file)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines++;
                            if (line.contains(keyword)) {
                                keywordLines++;
                            }
                        }
                    }

                    BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                    long size = attrs.size();

                    System.out.println("File: " + file.getFileName());
                    System.out.println("Size: " + size + " bytes");
                    System.out.println("Lines: " + lines);
                    System.out.println("Contains ERROR: " + keywordLines);
                    System.out.println();

                    totalLines += lines;
                    totalKeywordLines += keywordLines;
                }
            }

            System.out.println("===== Overall Result =====");
            System.out.println("Total Lines: " + totalLines);
            System.out.println("Total Lines Containing ERROR: " + totalKeywordLines);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
