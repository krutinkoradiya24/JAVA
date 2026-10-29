package model;

public interface InterestBearing {
    default double yearlyInterest(long balance, double interestRate) {
        return balance * interestRate / 100.0;
    }
}

