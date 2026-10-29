import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentServiceTest {
    @Test
    void addingStudentIncreasesCount() {
        StudentService service = new StudentService();
        service.addStudent(new Student(1, "Rahul", "CSE"));
        assertEquals(1, service.getCount());
    }

    @Test
    void addingTwoStudentsGivesCountTwo() {
        StudentService service = new StudentService();
        service.addStudent(new Student(1, "Rahul", "CSE"));
        service.addStudent(new Student(2, "Priya", "IT"));
        assertEquals(2, service.getCount());
    }

    @Test
    void addingNullStudentThrowsException() {
        StudentService service = new StudentService();
        assertThrows(IllegalArgumentException.class, () -> service.addStudent(null));
    }
}
