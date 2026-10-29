package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import repository.Repository;
import util.StatementFormatter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BankService {
    private final Repository<Account> repository = new Repository<>();

    public void openAccount(Account account) throws BankException {
        if (account == null) {
            throw new BankException("Account cannot be null.");
        }
        if (account.getOwnerName() == null || account.getOwnerName().trim().isEmpty()) {
            throw new BankException("Owner name cannot be empty.");
        }
        repository.save(account);
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = repository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account " + accountNumber + " not found.");
        }
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
    }

    public void transfer(String fromAccount, String toAccount, long amount) throws BankException {
        Account from = getAccount(fromAccount);
        Account to = getAccount(toAccount);
        from.transfer(to, amount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        Account account = getAccount(accountNumber);
        return StatementFormatter.buildStatement(account);
    }

    public List<Account> listAccounts() {
        ArrayList<Account> accounts = repository.findAll();
        accounts.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return accounts;
    }

    public void saveAccounts(Path file) {
        ArrayList<Account> list = repository.findAll();
        StatePersister.save(list.toArray(new Account[0]), file);
    }

    public void loadAccounts(Path file) {
        Account[] accounts = StatePersister.load(file);
        for (Account a : accounts) {
            repository.save(a);
        }
    }
}

