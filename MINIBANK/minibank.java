import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import model.BankInfo;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import service.BankService;
import service.ReportGenerator;
import service.TransactionLog;
import service.TransactionProcessor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class MiniBank {
    private static final Path DATA_FILE = Paths.get("accounts.dat");
    private static final Path LOGS_DIR = Paths.get("logs");
    private static final Path LOG_FILE = LOGS_DIR.resolve("transactions.log");
    private static final Path REPORT_FILE = LOGS_DIR.resolve("end_of_day_report.txt");

    public static void main(String[] args) {
        BankService service = new BankService();
        service.loadAccounts(DATA_FILE);

        if (service.listAccounts().isEmpty()) {
            try {
                service.openAccount(new SavingsAccount("Rahul", 10000, 1000));
                service.openAccount(new CurrentAccount("Amit", 25000, 5000));
                service.openAccount(new FixedDepositAccount("Krutin", 50000));
            } catch (BankException ignored) {}
        }

        BankInfo bankInfo = new BankInfo("MiniBank India", "Main Branch (CG Road)");
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println(" Welcome to " + bankInfo.name());
        System.out.println(" Branch     : " + bankInfo.branch());
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View Statement");
            System.out.println("6. List Accounts (Sorted by Balance)");
            System.out.println("7. Run Batch Transactions (Thread Pool)");
            System.out.println("8. Generate End-of-Day Report");
            System.out.println("9. Exit");
            System.out.print("Enter choice (1-9): ");

            if (!sc.hasNextInt()) {
                break;
            }
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\nSelect Account Type:");
                    System.out.println("1. Savings Account");
                    System.out.println("2. Current Account");
                    System.out.println("3. Fixed Deposit Account");
                    System.out.print("Choice: ");
                    int type = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Owner Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Initial Deposit: ");
                    long initBal = sc.nextLong();
                    sc.nextLine();

                    try {
                        Account acc = switch (type) {
                            case 1 -> new SavingsAccount(name, initBal);
                            case 2 -> new CurrentAccount(name, initBal);
                            case 3 -> new FixedDepositAccount(name, initBal);
                            default -> null;
                        };

                        if (acc != null) {
                            service.openAccount(acc);
                            TransactionLog.append(LOG_FILE, "OPEN " + acc.getAccountNumber() + " " + initBal);
                            System.out.println("Account opened successfully! Assigned Number: " + acc.getAccountNumber());
                        } else {
                            System.out.println("Invalid account type.");
                        }
                    } catch (BankException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Enter Account Number: ");
                    String accNum = sc.next();
                    System.out.print("Enter Amount to Deposit: ");
                    long amount = sc.nextLong();
                    sc.nextLine();

                    try {
                        service.deposit(accNum, amount);
                        TransactionLog.append(LOG_FILE, "DEPOSIT " + accNum + " " + amount);
                        System.out.println("Deposit successful. New Balance: Rs. " + service.getAccount(accNum).getBalance());
                    } catch (BankException e) {
                        System.out.println("Deposit failed: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter Account Number: ");
                    String accNum = sc.next();
                    System.out.print("Enter Amount to Withdraw: ");
                    long amount = sc.nextLong();
                    sc.nextLine();

                    try {
                        service.withdraw(accNum, amount);
                        TransactionLog.append(LOG_FILE, "WITHDRAW " + accNum + " " + amount);
                        System.out.println("Withdrawal successful. New Balance: Rs. " + service.getAccount(accNum).getBalance());
                    } catch (InsufficientFundsException e) {
                        System.out.println("Withdrawal failed: " + e.getMessage() + " (Shortfall: " + e.getShortfall() + ")");
                    } catch (BankException e) {
                        System.out.println("Withdrawal failed: " + e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Enter Source Account: ");
                    String from = sc.next();
                    System.out.print("Enter Destination Account: ");
                    String to = sc.next();
                    System.out.print("Enter Amount to Transfer: ");
                    long amount = sc.nextLong();
                    sc.nextLine();

                    try {
                        service.transfer(from, to, amount);
                        TransactionLog.append(LOG_FILE, "TRANSFER " + from + " -> " + to + " " + amount);
                        System.out.println("Transfer successful!");
                    } catch (BankException e) {
                        System.out.println("Transfer failed: " + e.getMessage());
                    }
                }
                case 5 -> {
                    System.out.print("Enter Account Number: ");
                    String accNum = sc.next();
                    sc.nextLine();
                    try {
                        System.out.println(service.statement(accNum));
                    } catch (BankException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 6 -> {
                    List<Account> accounts = service.listAccounts();
                    System.out.println("\nAll Accounts (Sorted by Balance descending):");
                    for (Account a : accounts) {
                        System.out.println(a);
                    }
                }
                case 7 -> {
                    System.out.println("Running batch transactions on thread pool...");
                    TransactionProcessor processor = new TransactionProcessor();
                    List<Account> accounts = service.listAccounts();
                    if (!accounts.isEmpty()) {
                        Account target = accounts.get(0);
                        for (int i = 0; i < 5; i++) {
                            processor.submit(() -> {
                                try {
                                    target.deposit(100);
                                    TransactionLog.append(LOG_FILE, "DEPOSIT " + target.getAccountNumber() + " 100");
                                } catch (Exception ignored) {}
                            });
                        }
                    }
                    processor.stop();
                    System.out.println("Batch execution completed.");
                }
                case 8 -> {
                    ReportGenerator.generateReport(LOGS_DIR, REPORT_FILE);
                }
                case 9 -> {
                    service.saveAccounts(DATA_FILE);
                    running = false;
                    System.out.println("Accounts saved. Thank you for using MiniBank. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please select from 1 to 9.");
            }
        }
        sc.close();
    }
}

