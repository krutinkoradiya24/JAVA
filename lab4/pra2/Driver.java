import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        String[] logs = {
                "10:05 alice Hello there",
                "10:10 bob How are you?",
                "InvalidLine",
                "10:20 charlie Nice to say hello"
        };

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        System.out.println();
        System.out.println(ChatFilter.filterLogs(logs, keyword));

        sc.close();
    }
}