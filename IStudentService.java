import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface defining the business logic and operations for managing student records.
 * Demonstrates Abstraction in Object-Oriented Programming.
 */
public interface IStudentService {

    /**
     * Adds a new student record to the system.
     *
     * @param student Student entity to add
     * @throws DuplicateStudentException   if a student with the same ID already exists
     * @throws InvalidStudentDataException if student data fails validation
     */
    void addStudent(Student student) throws DuplicateStudentException, InvalidStudentDataException;

    /**
     * Updates details of an existing student.
     *
     * @param id       Student ID
     * @param newName  Updated name
     * @param newMarks Updated marks (0 to 100)
     * @throws StudentNotFoundException    if student with the given ID does not exist
     * @throws InvalidStudentDataException if updated details fail validation
     */
    void updateStudent(int id, String newName, int newMarks) throws StudentNotFoundException, InvalidStudentDataException;

    /**
     * Deletes a student by ID.
     *
     * @param id Student ID
     * @return true if deleted
     * @throws StudentNotFoundException    if student does not exist
     * @throws InvalidStudentDataException if ID is invalid
     */
    boolean deleteStudent(int id) throws StudentNotFoundException, InvalidStudentDataException;

    /**
     * Finds a student by unique ID.
     *
     * @param id Student ID
     * @return Student instance
     * @throws StudentNotFoundException    if student does not exist
     * @throws InvalidStudentDataException if ID is invalid
     */
    Student searchById(int id) throws StudentNotFoundException, InvalidStudentDataException;

    /**
     * Searches students whose names contain the given query string (case-insensitive).
     *
     * @param query Search query
     * @return List of matching students
     */
    List<Student> searchByName(String query);

    /**
     * Retrieves all student records.
     *
     * @return List of all students
     */
    List<Student> getAllStudents();

    /**
     * Displays all student records to standard output.
     * Maintained for backward compatibility.
     */
    void viewStudents();

    /**
     * Returns students sorted using a custom Comparator.
     *
     * @param comparator Comparator to sort by
     * @return Sorted list of students
     */
    List<Student> getSortedStudents(Comparator<Student> comparator);

    /**
     * Filters students using a Predicate condition.
     *
     * @param predicate Predicate filter condition
     * @return List of matching students
     */
    List<Student> filterStudents(Predicate<Student> predicate);

    /**
     * Returns total count of students currently managed.
     *
     * @return Count of students
     */
    int getStudentCount();

    /**
     * Computes average marks across all students.
     *
     * @return Class average marks (0.0 if empty)
     */
    double getAverageMarks();

    /**
     * Returns the highest-scoring student.
     *
     * @return Top student or null if empty
     */
    Student getHighestScorer();

    /**
     * Returns the lowest-scoring student.
     *
     * @return Lowest student or null if empty
     */
    Student getLowestScorer();

    /**
     * Persists all current student records to the specified file path.
     *
     * @param filePath File path
     * @throws StorageException if an I/O error occurs
     */
    void saveToFile(String filePath) throws StorageException;

    /**
     * Loads student records from the specified file path.
     *
     * @param filePath File path
     * @throws StorageException if an I/O error occurs
     */
    void loadFromFile(String filePath) throws StorageException;
}
