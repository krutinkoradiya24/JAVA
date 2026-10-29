import java.util.ArrayList;

public class StudentService {
    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        students.add(student);
    }

    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public int getCount() {
        return students.size();
    }
}
