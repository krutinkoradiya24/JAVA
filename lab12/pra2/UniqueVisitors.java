/**
 * Student Name : Krutin
 * Enrolment No : 25CS034
 * Course       : CEUE203 - Object Oriented Programming
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class UniqueVisitors {
    public static void main(String[] args) {
        System.out.println("Enrolment No: 25CS034 | Name: Krutin");

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

        HashSet<String> uniqueUsers = new HashSet<>(loginNames);
        LinkedHashSet<String> firstSeenUsers = new LinkedHashSet<>(loginNames);

        System.out.println("Unique users: " + uniqueUsers.size());
        System.out.println("First three distinct names:");

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
