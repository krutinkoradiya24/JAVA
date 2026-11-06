package model;
import java.io.Serializable;
import java.util.Objects;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
public abstract class Account implements Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;
    @Id
    private final String accountNumber;
    private String ownerName;
    protected long balance;
    private boolean active;
    private static int accountCounter = 0;
    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }
    protected Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = Math.max(0, openingBalance);
        this.active = true;
    }
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive.");
        }
        if (!active) {
            throw new InvalidAmountException("This account is inactive.");
        }
        balance += amount;
    }
    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive.");
        }
        if (!active) {
            throw new InvalidAmountException("This account is inactive.");
        }
        if (amount > balance || !canWithdraw(amount)) {
            long shortfall = Math.max(0, amount - balance);
            throw new InsufficientFundsException("Withdrawal is not allowed.", shortfall);
        }
        balance -= amount;
    }
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public synchronized long getBalance() {
        return balance;
    }
    public boolean isActive() {
        return active;
    }
    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }
    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;
        Account other = (Account) obj;
        return accountNumber.equals(other.accountNumber);
    }
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
