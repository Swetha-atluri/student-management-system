/**
 * Represents a Student entity in the Student Management System.
 * Inherits from the abstract class Person, demonstrating OOP Inheritance.
 */
public class Student extends Person {

    private int marks;

    /**
     * Constructs a new Student instance.
     *
     * @param id    Unique student ID
     * @param name  Full name of the student
     * @param marks Marks obtained (0 to 100)
     */
    public Student(int id, String name, int marks) {
        super(id, name);
        this.marks = marks;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    /**
     * Computes the letter grade based on marks.
     *
     * @return Grade letter (A, B, C, D, or F)
     */
    public String getGrade() {
        if (marks >= 85) return "A";
        if (marks >= 70) return "B";
        if (marks >= 55) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    /**
     * Checks if the student passed (marks >= 40).
     *
     * @return true if passed, false otherwise
     */
    public boolean isPassed() {
        return marks >= 40;
    }

    @Override
    public String getRoleDescription() {
        return "Enrolled Student";
    }

    /**
     * Serializes student data to CSV format.
     *
     * @return Comma-separated representation of student
     */
    public String toCsv() {
        return id + "," + escapeCsv(name) + "," + marks;
    }

    /**
     * Deserializes a student from CSV format.
     *
     * @param csvLine Line of CSV text
     * @return Student instance
     * @throws IllegalArgumentException if format is invalid
     */
    public static Student fromCsv(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV line cannot be null or empty");
        }
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Malformed CSV line. Expected 3 fields: " + csvLine);
        }
        int id = Integer.parseInt(parts[0].trim());
        String name = unescapeCsv(parts[1].trim());
        int marks = Integer.parseInt(parts[2].trim());
        return new Student(id, name, marks);
    }

    private static String escapeCsv(String str) {
        if (str == null) return "";
        if (str.contains(",") || str.contains("\"")) {
            return "\"" + str.replace("\"", "\"\"") + "\"";
        }
        return str;
    }

    private static String unescapeCsv(String str) {
        if (str == null) return "";
        if (str.startsWith("\"") && str.endsWith("\"") && str.length() >= 2) {
            String inner = str.substring(1, str.length() - 1);
            return inner.replace("\"\"", "\"");
        }
        return str;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Marks: " + marks;
    }
}
