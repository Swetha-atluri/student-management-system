/**
 * Base exception for all domain-specific exceptions in the Student Management System.
 */
public class StudentException extends Exception {

    public StudentException(String message) {
        super(message);
    }

    public StudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
