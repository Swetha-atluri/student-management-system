import java.util.Comparator;

/**
 * Reusable utility class containing custom Comparator implementations for Student entities.
 * Demonstrates Java Collections, functional interfaces, and custom sorting strategies.
 */
public final class StudentComparators {

    private StudentComparators() {
        // Prevent instantiation
    }

    /**
     * Sorts students by ID in ascending order (1, 2, 3...).
     */
    public static final Comparator<Student> BY_ID_ASC = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            return Integer.compare(s1.getId(), s2.getId());
        }
    };

    /**
     * Sorts students by ID in descending order (...3, 2, 1).
     */
    public static final Comparator<Student> BY_ID_DESC = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            return Integer.compare(s2.getId(), s1.getId());
        }
    };

    /**
     * Sorts students alphabetically by Name in ascending order (A to Z), case-insensitive.
     */
    public static final Comparator<Student> BY_NAME_ASC = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            return s1.getName().compareToIgnoreCase(s2.getName());
        }
    };

    /**
     * Sorts students alphabetically by Name in descending order (Z to A), case-insensitive.
     */
    public static final Comparator<Student> BY_NAME_DESC = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            return s2.getName().compareToIgnoreCase(s1.getName());
        }
    };

    /**
     * Sorts students by Marks in descending order (highest scorer first).
     */
    public static final Comparator<Student> BY_MARKS_DESC = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            int marksComparison = Integer.compare(s2.getMarks(), s1.getMarks());
            if (marksComparison != 0) {
                return marksComparison;
            }
            // Tie breaker: alphabetical by name
            return s1.getName().compareToIgnoreCase(s2.getName());
        }
    };

    /**
     * Sorts students by Marks in ascending order (lowest scorer first).
     */
    public static final Comparator<Student> BY_MARKS_ASC = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            int marksComparison = Integer.compare(s1.getMarks(), s2.getMarks());
            if (marksComparison != 0) {
                return marksComparison;
            }
            // Tie breaker: alphabetical by name
            return s1.getName().compareToIgnoreCase(s2.getName());
        }
    };
}
