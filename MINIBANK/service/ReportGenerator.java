package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    public static void generateReport(Path logsFolder, Path reportFile) {
        if (!Files.exists(logsFolder)) {
            System.out.println("Logs folder does not exist.");
            return;
        }

        long totalDeposits = 0;
        long totalWithdrawals = 0;
        long totalTransfers = 0;
        int filesCount = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsFolder, "*.log")) {
            for (Path logFile : stream) {
                filesCount++;
                try (BufferedReader reader = Files.newBufferedReader(logFile)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 3) {
                            String type = parts[0].toUpperCase();
                            try {
                                long amount = Long.parseLong(parts[2]);
                                if (type.contains("DEPOSIT")) {
                                    totalDeposits += amount;
                                } else if (type.contains("WITHDRAW")) {
                                    totalWithdrawals += amount;
                                } else if (type.contains("TRANSFER")) {
                                    totalTransfers += amount;
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }

            List<String> reportLines = new ArrayList<>();
            reportLines.add("=========================================");
            reportLines.add("        MINIBANK END-OF-DAY REPORT       ");
            reportLines.add("=========================================");
            reportLines.add("Log files parsed : " + filesCount);
            reportLines.add("Total Deposits   : Rs. " + totalDeposits);
            reportLines.add("Total Withdrawals: Rs. " + totalWithdrawals);
            reportLines.add("Total Transfers  : Rs. " + totalTransfers);
            reportLines.add("Net Balance Flow : Rs. " + (totalDeposits - totalWithdrawals));
            reportLines.add("=========================================");

            if (reportFile.getParent() != null && !Files.exists(reportFile.getParent())) {
                Files.createDirectories(reportFile.getParent());
            }
            Files.write(reportFile, reportLines);

            for (String r : reportLines) {
                System.out.println(r);
            }
            System.out.println("Report written to: " + reportFile.getFileName());

        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}

