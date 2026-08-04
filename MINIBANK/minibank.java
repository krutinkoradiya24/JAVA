import java.util.*;

record BankInfo(String name, String branch) {
}

enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public class minibank {

    public static void main(String[] args) {

        Account[] accounts = {
            new Account("parth", 5000),
            new Account("Rahul", 3000),
            new Account("jay", 7000)
        };

        accounts[0].deposit(1000);
        accounts[1].withdraw(500);
        accounts[2].deposit(2000);
        accounts[2].withdraw(1000);

        for (Account account : accounts) {
            System.out.println(account.getAccountNumber() + " : " + account.getBalance());
        }

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("BOI", "Main Branch");

        System.out.println("=================================");
        System.out.println(bank);
        System.out.println("=================================");

        int choice;

        do {
            System.out.println();
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    System.out.println("Open Account - To be implemented in a later lab");

                case DEPOSIT ->
                    System.out.println("Deposit - To be implemented in a later lab");

                case WITHDRAW ->
                    System.out.println("Withdraw - To be implemented in a later lab");

                case TRANSFER ->
                    System.out.println("Transfer - To be implemented in a later lab");

                case EXIT ->
                    System.out.println("Thank you for using MiniBank. Goodbye!");
            }

        } while (choice != 5);

        sc.close();
    }
}