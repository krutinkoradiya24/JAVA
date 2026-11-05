public class ChatFilter {
    public static String filterLogs(String[] logs, String keyword) {
        StringBuilder report = new StringBuilder();
        int count = 0;
        keyword = keyword.toLowerCase();
        for (String line : logs) {
            // Split into time, user, and message
            String[] parts = line.split(" ", 3);
            if (parts.length < 3) {
                continue;
            }
            String time = parts[0];
            String user = parts[1];
            String message = parts[2];
            // Case-insensitive check
            if (message.toLowerCase().contains(keyword)) {
                count++;
                report.append(time)
                      .append(" ")
                      .append(user)
                      .append(": ")
                      .append(message)
                      .append("\n");
            }
        }
        return "Matches: " + count + "\n" + report.toString();
    }
}
