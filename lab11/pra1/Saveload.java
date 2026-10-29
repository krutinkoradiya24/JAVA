import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Student implements Serializable {
    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("ID: " + id + ", Name: " + name + ", Password: " + password);
    }
}

public class Saveload {
    public static void main(String[] args) {
        Student[] students = {
            new Student(101, "Krutin", "abc123"),
            new Student(102, "Rahul", "xyz456")
        };

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            out.writeObject(students);
            System.out.println("Objects saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("students.dat"))) {
            Student[] loadedStudents = (Student[]) in.readObject();
            System.out.println("\nObjects loaded successfully:");
            for (Student s : loadedStudents) {
                s.display();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
