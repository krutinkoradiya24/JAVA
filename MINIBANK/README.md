# MiniBank Application

MiniBank is a modular console-based banking application developed in Java following Object-Oriented Programming (CEUE203) principles.

## Architecture

- **`model`**: Contains entities (`Account`, `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`, `Customer`), records (`BankInfo`, `Command`), and interfaces (`Transactable`, `InterestBearing`, `WithdrawRule`, `Premium`).
- **`model.annotation`**: Custom runtime annotations (`@Id`, `@Positive`, `@MaxLength`).
- **`exception`**: Custom checked exception hierarchy (`BankException`, `InsufficientFundsException`, `AccountNotFoundException`, `InvalidAmountException`).
- **`util`**: Validation (`Validator`), reflective validation (`AnnotationValidator`), parser (`CommandParser`), and formatting (`StatementFormatter`).
- **`repository`**: Reusable generic `Repository<T>` with reflective `@Id` lookup and thread-safe storage.
- **`service`**: Business operations (`BankService`), multi-threaded transaction engine (`TransactionProcessor`), audit logger (`TransactionLog`), end-of-day report generator (`ReportGenerator`), and persistence (`StatePersister`).
- **`MiniBank.java`**: Main interactive console application.

## How to Compile and Run

### 1. Compile
```bash
javac model\annotation\*.java exception\*.java model\*.java util\*.java repository\*.java service\*.java MiniBank.java
```

### 2. Run
```bash
java MiniBank
```

### 3. Run Executable JAR
```bash
java -jar minibank.jar
```

