import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service implementation managing student business operations.
 * Implements IStudentService and maintains both an ArrayList and a HashMap
 * to demonstrate Java Collections: ArrayList for ordered sequences and HashMap
 * for O(1) key-based lookups.
 */
public class StudentService implements IStudentService {

    private static final Logger LOGGER = Logger.getLogger(StudentService.class.getName());

    // Maintained for backward compatibility and sequence order
    ArrayList<Student> studentList = new ArrayList<>();

    // HashMap for high-performance O(1) ID lookups
    private final Map<Integer, Student> studentMap = new HashMap<>();

    /**
     * Default constructor.
     */
    public StudentService() {
    }

    /**
     * Adds a student to the system.
     * Prevents duplicate IDs and validates data fields.
     *
     * @param s Student to add
     * @throws DuplicateStudentException   if ID already exists
     * @throws InvalidStudentDataException if student data is invalid
     */
    @Override
    public void addStudent(Student s) throws DuplicateStudentException, InvalidStudentDataException {
        if (s == null) {
            throw new InvalidStudentDataException("student", "Student record cannot be null.");
        }

        // Validate fields
        ValidationUtils.validateId(s.getId());
        String cleanName = ValidationUtils.validateAndCleanName(s.getName());
        s.setName(cleanName);
        ValidationUtils.validateMarks(s.getMarks());

        // Check for duplicate ID
        if (studentMap.containsKey(s.getId())) {
            LOGGER.warning("Duplicate student ID detected: " + s.getId());
            throw new DuplicateStudentException(s.getId());
        }

        // Add to both collections to maintain consistency
        studentList.add(s);
        studentMap.put(s.getId(), s);

        LOGGER.info("Student added: ID=" + s.getId() + ", Name=" + s.getName());
        System.out.println("Student added successfully");
    }

    /**
     * Updates an existing student's name and marks.
     *
     * @param id       Student ID to update
     * @param newName  New student name
     * @param newMarks New student marks
     * @throws StudentNotFoundException    if student is not found
     * @throws InvalidStudentDataException if updated fields are invalid
     */
    @Override
    public void updateStudent(int id, String newName, int newMarks)
            throws StudentNotFoundException, InvalidStudentDataException {
        ValidationUtils.validateId(id);
        String cleanName = ValidationUtils.validateAndCleanName(newName);
        ValidationUtils.validateMarks(newMarks);

        Student s = studentMap.get(id);
        if (s == null) {
            System.out.println("Student not found");
            throw new StudentNotFoundException(id);
        }

        s.setName(cleanName);
        s.setMarks(newMarks);

        LOGGER.info("Student updated: ID=" + id + ", NewName=" + cleanName + ", NewMarks=" + newMarks);
        System.out.println("Student updated successfully");
    }

    /**
     * Deletes a student from both the list and the map.
     * Demonstrates safe collection modification using Iterator.
     *
     * @param id Student ID to remove
     * @return true if successfully deleted
     * @throws StudentNotFoundException if student ID does not exist
     */
    @Override
    public boolean deleteStudent(int id) throws StudentNotFoundException, InvalidStudentDataException {
        ValidationUtils.validateId(id);

        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }

        studentMap.remove(id);

        // Safe removal from ArrayList using Iterator
        Iterator<Student> iterator = studentList.iterator();
        while (iterator.hasNext()) {
            Student s = iterator.next();
            if (s.getId() == id) {
                iterator.remove();
                break;
            }
        }

        LOGGER.info("Student deleted: ID=" + id);
        return true;
    }

    /**
     * Searches for a student by ID using the HashMap for O(1) lookup.
     *
     * @param id Student ID
     * @return Student record
     * @throws StudentNotFoundException    if student is not found
     * @throws InvalidStudentDataException if ID is invalid
     */
    @Override
    public Student searchById(int id) throws StudentNotFoundException, InvalidStudentDataException {
        ValidationUtils.validateId(id);
        Student s = studentMap.get(id);
        if (s == null) {
            throw new StudentNotFoundException(id);
        }
        return s;
    }

    /**
     * Searches for students by full or partial name (case-insensitive).
     *
     * @param query Search string
     * @return List of matching students
     */
    @Override
    public List<Student> searchByName(String query) {
        List<Student> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        String lowerQuery = query.trim().toLowerCase();
        for (Student s : studentList) {
            if (s.getName().toLowerCase().contains(lowerQuery)) {
                results.add(s);
            }
        }
        return results;
    }

    /**
     * Returns an unmodifiable copy of all students.
     *
     * @return List of students
     */
    @Override
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(new ArrayList<>(studentList));
    }

    /**
     * Displays all students to standard output.
     * Preserves original method signature and output format.
     */
    @Override
    public void viewStudents() {
        if (studentList.isEmpty()) {
            System.out.println("No students found");
        } else {
            for (Student s : studentList) {
                System.out.println(s);
            }
        }
    }

    /**
     * Returns a sorted list of students using the provided Comparator.
     * Original list order is preserved.
     *
     * @param comparator Comparator implementation
     * @return New sorted ArrayList of students
     */
    @Override
    public List<Student> getSortedStudents(Comparator<Student> comparator) {
        List<Student> sortedList = new ArrayList<>(studentList);
        if (comparator != null) {
            sortedList.sort(comparator);
        }
        return sortedList;
    }

    /**
     * Filters students using a Predicate condition.
     * Demonstrates Java functional interfaces and loops.
     *
     * @param predicate Condition
     * @return List of matching students
     */
    @Override
    public List<Student> filterStudents(Predicate<Student> predicate) {
        List<Student> filtered = new ArrayList<>();
        if (predicate == null) {
            return filtered;
        }
        for (Student s : studentList) {
            if (predicate.test(s)) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    @Override
    public int getStudentCount() {
        return studentList.size();
    }

    @Override
    public double getAverageMarks() {
        if (studentList.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (Student s : studentList) {
            total += s.getMarks();
        }
        return (double) total / studentList.size();
    }

    @Override
    public Student getHighestScorer() {
        if (studentList.isEmpty()) return null;
        Student highest = studentList.get(0);
        for (Student s : studentList) {
            if (s.getMarks() > highest.getMarks()) {
                highest = s;
            }
        }
        return highest;
    }

    @Override
    public Student getLowestScorer() {
        if (studentList.isEmpty()) return null;
        Student lowest = studentList.get(0);
        for (Student s : studentList) {
            if (s.getMarks() < lowest.getMarks()) {
                lowest = s;
            }
        }
        return lowest;
    }

    @Override
    public void saveToFile(String filePath) throws StorageException {
        FileStorageUtil.saveStudents(studentList, filePath);
    }

    @Override
    public void loadFromFile(String filePath) throws StorageException {
        List<Student> loaded = FileStorageUtil.loadStudents(filePath);
        studentList.clear();
        studentMap.clear();

        for (Student s : loaded) {
            if (!studentMap.containsKey(s.getId())) {
                studentList.add(s);
                studentMap.put(s.getId(), s);
            } else {
                LOGGER.warning("Duplicate ID encountered while loading from file: " + s.getId() + ". Skipping.");
            }
        }
        LOGGER.info("Loaded " + studentList.size() + " students into memory.");
    }
}
