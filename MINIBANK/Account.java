class Account {

    private static int nextAccountNumber = 1001;

    private int accountNumber;
    private String ownerName;
    private double balance;

    public Account(String ownerName, double balance) {
        this.accountNumber = nextAccountNumber++;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient Balance");
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
}