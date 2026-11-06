package service;
import model.Account;
public class StatementFormatter {
    public static String buildStatement(Account account) {
        StringBuilder result = new StringBuilder();
        result.append("------ Account Statement ------\n")
            .append("Account number: ")
            .append(account.getAccountNumber())
            .append('\n')
            .append("Owner: ")
            .append(account.getOwnerName())
            .append('\n')
            .append("Balance: ")
            .append(account.getBalance())
            .append('\n')
            .append("Active: ")
            .append(account.isActive())
            .append('\n')
            .append("-------------------------------");
        return result.toString();
    }
}
