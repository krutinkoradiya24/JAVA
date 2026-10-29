package service;

import model.Account;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatePersister {
    public static void save(Account[] accounts, Path file) {
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException ignored) {}
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
            out.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    public static Account[] load(Path file) {
        if (!Files.exists(file)) {
            return new Account[0];
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.toFile()))) {
            return (Account[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Load error: " + e.getMessage());
            return new Account[0];
        }
    }
}

