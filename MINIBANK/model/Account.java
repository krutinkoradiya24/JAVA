package model;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.Positive;

import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static long accountCounter = 0;

    @Id
    private final String accountNumber;
    private String ownerName;

    @Positive
    private long balance;
    private boolean active;

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        this.balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if (!canWithdraw(amount)) {
            long shortfall = amount - this.balance;
            throw new InsufficientFundsException("Insufficient funds for withdrawal. Shortfall: " + Math.max(0, shortfall), shortfall);
        }
        this.balance -= amount;
        return true;
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new BankException("Recipient account cannot be null.");
        }
        if (this.accountNumber.equals(to.accountNumber)) {
            throw new BankException("Cannot transfer to the same account.");
        }

        Account first = this.accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = this.accountNumber.compareTo(to.accountNumber) < 0 ? to : this;

        synchronized (first) {
            synchronized (second) {
                this.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public double yearlyInterest() {
        return (balance * interestRate()) / 100.0;
    }

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

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " | " + (active ? "ACTIVE" : "INACTIVE");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public int compareTo(Account o) {
        return this.accountNumber.compareTo(o.accountNumber);
    }
}

