import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
// Unit test cases for StudentService
public class StudentServiceTest {
    // Test adding single student
    @Test
    void addingStudentIncreasesCount() {
        StudentService service = new StudentService();
        service.addStudent(new Student(1, "Rahul", "CSE"));
        assertEquals(1, service.getCount());
    }
    // Test adding multiple students
    @Test
    void addingTwoStudentsGivesCountTwo() {
        StudentService service = new StudentService();
        service.addStudent(new Student(1, "Rahul", "CSE"));
        service.addStudent(new Student(2, "Priya", "IT"));
        assertEquals(2, service.getCount());
    }
    // Test invalid null student validation
    @Test
    void addingNullStudentThrowsException() {
        StudentService service = new StudentService();
        assertThrows(IllegalArgumentException.class, () -> service.addStudent(null));
    }
}
