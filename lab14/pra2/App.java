import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

class StudentRecord implements Serializable {
    private int id;
    private String name;

    public StudentRecord(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}

public class App {
    public static void main(String[] args) {
        ArrayList<StudentRecord> list = new ArrayList<>();
        list.add(new StudentRecord(101, "Rahul"));
        list.add(new StudentRecord(102, "Priya"));

        String filename = "data.dat";

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(list);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            @SuppressWarnings("unchecked")
            ArrayList<StudentRecord> loaded = (ArrayList<StudentRecord>) in.readObject();
            System.out.println("Data reloaded successfully:");
            for (StudentRecord s : loaded) {
                System.out.println(s);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

