/**
 * Thrown when student input data violates domain validation rules
 * (e.g., negative ID, empty name, marks out of range).
 */
public class InvalidStudentDataException extends StudentException {

    private final String fieldName;

    public InvalidStudentDataException(String fieldName, String message) {
        super("Validation error on field '" + fieldName + "': " + message);
        this.fieldName = fieldName;
    }

    public InvalidStudentDataException(String message) {
        super(message);
        this.fieldName = "unknown";
    }

    public String getFieldName() {
        return fieldName;
    }
}
