import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> loginNames = Arrays.asList(
            "Krutin",
            "Rahul",
            "Amit",
            "Krutin",
            "Neha",
            "Rahul",
            "Priya",
            "Jay"
        );
        // HashSet for counting unique users
        HashSet<String> uniqueUsers = new HashSet<>(loginNames);
        // LinkedHashSet to preserve first-seen order
        LinkedHashSet<String> firstSeenUsers = new LinkedHashSet<>(loginNames);
        System.out.println("Unique users: " + uniqueUsers.size());
        System.out.println("First three distinct names:");
        // Print first 3 unique users
        int count = 0;
        for (String name : firstSeenUsers) {
            System.out.println(name);
            count++;
            if (count == 3) {
                break;
            }
        }
    }
}
