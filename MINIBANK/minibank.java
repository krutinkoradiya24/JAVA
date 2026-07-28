import java.util.Scanner;
record BankInfo(String name, String branch) { }
enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}
public class minibank{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("Mini Bank", "Ahmedabad Branch");
        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        int choice;

        while (true) {

            System.out.println("\n----- MiniBank Menu -----");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Open Account - To be implemented in a later lab.");
                case 2 -> System.out.println("Deposit - To be implemented in a later lab.");
                case 3 -> System.out.println("Withdraw - To be implemented in a later lab.");
                case 4 -> System.out.println("Transfer - To be implemented in a later lab.");
                case 5 -> {
                    System.out.println("Thank you for using MiniBank!");
                    break;
                }
                default -> System.out.println("Invalid choice!");
            }

            if (choice == 5) {
                break;
            }
        }

        sc.close();
    }
}