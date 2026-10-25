import java.util.regex.Pattern;

/**
 * Reusable utility class for input validation and safe type conversions.
 * Demonstrates string manipulation, regular expressions, and wrapper class type safety.
 */
public final class ValidationUtils {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-'.]+$");
    public static final int MIN_MARKS = 0;
    public static final int MAX_MARKS = 100;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;

    private ValidationUtils() {
        // Prevent instantiation of utility class
    }

    /**
     * Validates student ID. Must be positive.
     *
     * @param id Student ID
     * @throws InvalidStudentDataException if ID is invalid
     */
    public static void validateId(Integer id) throws InvalidStudentDataException {
        if (id == null) {
            throw new InvalidStudentDataException("id", "Student ID cannot be null.");
        }
        if (id <= 0) {
            throw new InvalidStudentDataException("id", "Student ID must be a positive integer greater than zero. Received: " + id);
        }
    }

    /**
     * Validates and cleans student name.
     *
     * @param name Name string
     * @return Cleaned, trimmed name
     * @throws InvalidStudentDataException if name is blank, too short/long, or contains invalid characters
     */
    public static String validateAndCleanName(String name) throws InvalidStudentDataException {
        if (name == null) {
            throw new InvalidStudentDataException("name", "Student name cannot be null.");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidStudentDataException("name", "Student name cannot be empty or blank.");
        }
        if (trimmed.length() < MIN_NAME_LENGTH || trimmed.length() > MAX_NAME_LENGTH) {
            throw new InvalidStudentDataException("name", "Student name must be between " +
                    MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters. Received: " + trimmed.length());
        }
        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            throw new InvalidStudentDataException("name", "Student name contains invalid characters. Only letters, spaces, hyphens, and periods are allowed.");
        }
        return trimmed;
    }

    /**
     * Validates student marks. Must be between 0 and 100 inclusive.
     *
     * @param marks Marks obtained
     * @throws InvalidStudentDataException if marks are out of range
     */
    public static void validateMarks(Integer marks) throws InvalidStudentDataException {
        if (marks == null) {
            throw new InvalidStudentDataException("marks", "Marks cannot be null.");
        }
        if (marks < MIN_MARKS || marks > MAX_MARKS) {
            throw new InvalidStudentDataException("marks", "Marks must be between " +
                    MIN_MARKS + " and " + MAX_MARKS + ". Received: " + marks);
        }
    }

    /**
     * Safely parses a string into an Integer wrapper.
     *
     * @param input Raw string
     * @return Integer object
     * @throws NumberFormatException if input cannot be parsed
     */
    public static Integer parseIntegerSafe(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("Input string is empty or null.");
        }
        return Integer.valueOf(input.trim());
    }

    /**
     * Safely parses a string into a Double wrapper.
     *
     * @param input Raw string
     * @return Double object
     * @throws NumberFormatException if input cannot be parsed
     */
    public static Double parseDoubleSafe(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            throw new NumberFormatException("Input string is empty or null.");
        }
        return Double.valueOf(input.trim());
    }
}
