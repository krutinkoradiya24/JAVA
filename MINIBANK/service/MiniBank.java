package service;
import exception.BankException;
import model.Account;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
public class MiniBank {
    private static final Path ACCOUNTS_FILE = Path.of("data", "accounts.dat");
    public static void main(String[] args) {
        Repository<Account> repository = new Repository<>();
        loadAccounts(repository);
        BankService bank = new BankService(repository);
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n--- MiniBank ---");
            System.out.println("1. Open account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Account statement");
            System.out.println("6. List accounts");
            System.out.println("7. Generate report");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = readInt(input);
            try {
                switch (choice) {
                    case 1 -> openAccount(input, bank);
                    case 2 -> {
                        System.out.print("Account number: ");
                        String id = input.next();
                        System.out.print("Amount: ");
                        long amount = readLong(input);
                        bank.deposit(id, amount);
                        System.out.println("Deposit successful.");
                    }
                    case 3 -> {
                        System.out.print("Account number: ");
                        String id = input.next();
                        System.out.print("Amount: ");
                        long amount = readLong(input);
                        bank.withdraw(id, amount);
                        System.out.println("Withdrawal successful.");
                    }
                    case 4 -> {
                        System.out.print("From account: ");
                        String from = input.next();
                        System.out.print("To account: ");
                        String to = input.next();
                        System.out.print("Amount: ");
                        long amount = readLong(input);
                        bank.transfer(from, to, amount);
                        System.out.println("Transfer successful.");
                    }
                    case 5 -> {
                        System.out.print("Account number: ");
                        System.out.println(bank.statement(input.next()));
                    }
                    case 6 -> printAccounts(bank.listAccounts());
                    case 7 -> System.out.println(
                        ReportGenerator.generate(
                            Path.of("data", "logs"),
                            Path.of("data", "report.txt")
                        )
                    );
                    case 8 -> {
                        saveAccounts(repository);
                        System.out.println("Thank you for using MiniBank!");
                        System.out.println("Enrollment Number: 25CS034");
                        running = false;
                    }
                    default -> System.out.println("Invalid menu choice.");
                }
            } catch (BankException | IOException exception) {
                System.out.println("Error: " + exception.getMessage());
            } catch (ClassNotFoundException exception) {
                System.out.println("Could not load saved accounts.");
            }
        }
        input.close();
    }
    private static void openAccount(Scanner input, BankService bank) throws BankException {
        input.nextLine();
        System.out.print("Customer name: ");
        String name = input.nextLine();
        System.out.print("Email: ");
        String email = input.nextLine();
        System.out.print("Mobile: ");
        String mobile = input.nextLine();
        System.out.print("Opening balance: ");
        long balance = readLong(input);
        Account account = bank.openAccount(name, email, mobile, balance);
        System.out.println("Account created: " + account.getAccountNumber());
    }
    private static void printAccounts(ArrayList<Account> accounts) {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        for (Account account : accounts) {
            System.out.println(account);
        }
    }
    private static void loadAccounts(Repository<Account> repository) {
        try {
            Account[] saved = StatePersister.load(ACCOUNTS_FILE);
            for (Account account : saved) {
                repository.save(account);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println("No saved accounts found; starting fresh.");
        }
    }
    private static void saveAccounts(Repository<Account> repository) throws IOException, ClassNotFoundException {
        ArrayList<Account> list = repository.findAll();
        Account[] array = list.toArray(new Account[0]);
        StatePersister.save(array, ACCOUNTS_FILE);
    }
    private static int readInt(Scanner input) {
        while (!input.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            input.next();
        }
        return input.nextInt();
    }
    private static long readLong(Scanner input) {
        while (!input.hasNextLong()) {
            System.out.print("Enter a valid whole amount: ");
            input.next();
        }
        return input.nextLong();
    }
}
