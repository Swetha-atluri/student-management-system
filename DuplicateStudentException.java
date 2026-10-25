/**
 * Thrown when attempting to add or register a student whose ID already exists in the system.
 */
public class DuplicateStudentException extends StudentException {

    private final int studentId;

    public DuplicateStudentException(int studentId) {
        super("Student with ID " + studentId + " already exists. Duplicate IDs are not permitted.");
        this.studentId = studentId;
    }

    public int getStudentId() {
        return studentId;
    }
}
