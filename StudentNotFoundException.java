/**
 * Thrown when a student with a specified ID or identifier is not found.
 */
public class StudentNotFoundException extends StudentException {

    private final int studentId;

    public StudentNotFoundException(int studentId) {
        super("Student with ID " + studentId + " was not found.");
        this.studentId = studentId;
    }

    public StudentNotFoundException(String message) {
        super(message);
        this.studentId = -1;
    }

    public int getStudentId() {
        return studentId;
    }
}
