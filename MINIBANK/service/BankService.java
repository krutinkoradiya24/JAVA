package service;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.SavingsAccount;
import util.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
public class BankService {
    private final Repository<Account> accounts;
    public BankService(Repository<Account> accounts) {
        this.accounts = accounts;
    }
    public Account openAccount(String name, String email, String mobile, long openingBalance) throws BankException {
        if (name == null || name.isBlank()) {
            throw new BankException("Name cannot be blank.");
        }
        if (!Validator.isValidEmail(email)) {
            throw new BankException("Invalid email address.");
        }
        if (!Validator.isValidMobile(mobile)) {
            throw new BankException("Invalid mobile number.");
        }
        if (openingBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }
        Account account = new SavingsAccount(name, openingBalance, 0);
        accounts.save(account);
        return account;
    }
    public void deposit(String accountNumber, long amount) throws BankException, IOException {
        Account account = findAccount(accountNumber);
        account.deposit(amount);
        TransactionLog.append(
            Path.of("data", "logs", "transactions.log"),
            "DEPOSIT " + accountNumber + " " + amount
        );
    }
    public void withdraw(String accountNumber, long amount) throws BankException, IOException {
        Account account = findAccount(accountNumber);
        account.withdraw(amount);
        TransactionLog.append(
            Path.of("data", "logs", "transactions.log"),
            "WITHDRAW " + accountNumber + " " + amount
        );
    }
    public void transfer(String fromNumber, String toNumber, long amount) throws BankException, IOException {
        if (!Validator.isValidAmount(amount)) {
            throw new InvalidAmountException("Transfer amount must be positive.");
        }
        Account from = findAccount(fromNumber);
        Account to = findAccount(toNumber);
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
        TransactionLog.append(
            Path.of("data", "logs", "transactions.log"),
            "WITHDRAW " + fromNumber + " " + amount
        );
        TransactionLog.append(
            Path.of("data", "logs", "transactions.log"),
            "DEPOSIT " + toNumber + " " + amount
        );
    }
    public String statement(String accountNumber) throws BankException {
        Account account = findAccount(accountNumber);
        return StatementFormatter.buildStatement(account);
    }
    public ArrayList<Account> listAccounts() {
        return accounts.findAll();
    }
    private Account findAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }
}
