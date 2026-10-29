package model;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    private long minBalance;

    public SavingsAccount(String ownerName, long balance, long minBalance) {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    public SavingsAccount(String ownerName, long balance) {
        this(ownerName, balance, 1000);
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return (getBalance() - amount) >= minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }

    @Override
    public String toString() {
        return "[Savings] " + super.toString() + " | MinBal: " + minBalance;
    }
}

