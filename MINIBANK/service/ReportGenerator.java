package service;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
public class ReportGenerator {
    public static String generate(Path logFolder, Path reportFile) throws IOException {
        long deposits = 0;
        long withdrawals = 0;
        StringBuilder details = new StringBuilder();
        if (Files.exists(logFolder)) {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(logFolder, "*.log")) {
                for (Path file : files) {
                    BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
                    details.append(file.getFileName())
                        .append(" | ")
                        .append(attributes.size())
                        .append(" bytes | ")
                        .append(attributes.lastModifiedTime())
                        .append('\n');
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.trim().split("\\s+");
                            if (parts.length < 3) {
                                continue;
                            }
                            try {
                                long amount = Long.parseLong(parts[2]);
                                if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                    deposits += amount;
                                } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                    withdrawals += amount;
                                }
                            } catch (NumberFormatException ignored) {
                                // Ignore malformed log amounts.
                            }
                        }
                    }
                }
            }
        }
        String report = "Total deposits: " + deposits + "\n"
            + "Total withdrawals: " + withdrawals + "\n"
            + "Net change: " + (deposits - withdrawals) + "\n\n"
            + "Log files:\n" + details;
        Path parent = reportFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(
            reportFile,
            report.getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
        return report;
    }
}
