import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Automated test suite for the Student Management System.
 * Tests core business logic, collections, sorting, searching, persistence,
 * validation, exception handling, and edge cases.
 */
public class StudentManagementTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" RUNNING AUTOMATED STUDENT MANAGEMENT TEST SUITE ");
        System.out.println("=================================================");

        testAddAndDisplayStudents();
        testDuplicateStudentPrevention();
        testSearchStudentById();
        testSearchStudentByName();
        testUpdateStudent();
        testDeleteStudent();
        testSortingComparators();
        testFilteringLogic();
        testInputValidation();
        testAnalyticsCalculation();
        testFilePersistenceAndReload();
        testCorruptFileHandling();
        testWrapperClassParsing();

        System.out.println("\n=================================================");
        System.out.println(" TEST RESULTS SUMMARY: " + testsPassed + " PASSED, " + testsFailed + " FAILED");
        System.out.println("=================================================");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("  [FAIL] " + testName);
            testsFailed++;
        }
    }

    private static void testAddAndDisplayStudents() {
        System.out.println("\n[Test Suite] 1. Adding Students and Collection Tracking");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(101, "Alice Smith", 88));
            service.addStudent(new Student(102, "Bob Jones", 74));
            assertTrue("Student count should be 2", service.getStudentCount() == 2);
            assertTrue("Alice should be in service", service.searchById(101).getName().equals("Alice Smith"));
            assertTrue("Bob should be in service", service.searchById(102).getName().equals("Bob Jones"));
        } catch (Exception e) {
            assertTrue("Unexpected exception during add: " + e.getMessage(), false);
        }
    }

    private static void testDuplicateStudentPrevention() {
        System.out.println("\n[Test Suite] 2. Duplicate Student ID Prevention");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(1, "Charlie Brown", 80));
            boolean exceptionThrown = false;
            try {
                // Attempt adding duplicate ID 1
                service.addStudent(new Student(1, "Charlie Duplicate", 95));
            } catch (DuplicateStudentException e) {
                exceptionThrown = true;
                assertTrue("Duplicate exception mentions ID 1", e.getStudentId() == 1);
            }
            assertTrue("DuplicateStudentException must be thrown on existing ID", exceptionThrown);
            assertTrue("Student count remains 1", service.getStudentCount() == 1);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testSearchStudentById() {
        System.out.println("\n[Test Suite] 3. Student Search by ID");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(201, "Diana Prince", 95));
            Student found = service.searchById(201);
            assertTrue("Found student by ID", found != null && found.getName().equals("Diana Prince"));

            boolean notFoundThrown = false;
            try {
                service.searchById(999);
            } catch (StudentNotFoundException e) {
                notFoundThrown = true;
                assertTrue("Exception has missing ID 999", e.getStudentId() == 999);
            }
            assertTrue("StudentNotFoundException thrown for missing ID", notFoundThrown);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testSearchStudentByName() {
        System.out.println("\n[Test Suite] 4. Student Search by Name (Case-Insensitive Substring)");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(1, "John Doe", 65));
            service.addStudent(new Student(2, "Jane Doe", 85));
            service.addStudent(new Student(3, "Bruce Wayne", 92));

            List<Student> doeList = service.searchByName("doe");
            assertTrue("Found 2 students with 'doe'", doeList.size() == 2);

            List<Student> wayneList = service.searchByName("BRUCE");
            assertTrue("Found Bruce Wayne with upper case query", wayneList.size() == 1 && wayneList.get(0).getId() == 3);

            List<Student> emptyList = service.searchByName("NonExistent");
            assertTrue("Empty list for unmatched name query", emptyList.isEmpty());
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testUpdateStudent() {
        System.out.println("\n[Test Suite] 5. Student Record Update");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(50, "Clark Kent", 60));
            service.updateStudent(50, "Superman", 100);

            Student updated = service.searchById(50);
            assertTrue("Name updated to Superman", "Superman".equals(updated.getName()));
            assertTrue("Marks updated to 100", updated.getMarks() == 100);
            assertTrue("Grade updated to A", "A".equals(updated.getGrade()));

            boolean notFound = false;
            try {
                service.updateStudent(999, "Nobody", 50);
            } catch (StudentNotFoundException e) {
                notFound = true;
            }
            assertTrue("Update non-existent ID throws StudentNotFoundException", notFound);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testDeleteStudent() {
        System.out.println("\n[Test Suite] 6. Student Record Deletion & Cache Sync");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(10, "Peter Parker", 78));
            service.addStudent(new Student(20, "Miles Morales", 88));

            boolean deleted = service.deleteStudent(10);
            assertTrue("Delete operation returns true", deleted);
            assertTrue("Student count decreased to 1", service.getStudentCount() == 1);

            boolean notFound = false;
            try {
                service.searchById(10);
            } catch (StudentNotFoundException e) {
                notFound = true;
            }
            assertTrue("Deleted student no longer searchable in map", notFound);

            boolean deleteMissingThrows = false;
            try {
                service.deleteStudent(999);
            } catch (StudentNotFoundException e) {
                deleteMissingThrows = true;
            }
            assertTrue("Deleting non-existent ID throws StudentNotFoundException", deleteMissingThrows);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testSortingComparators() {
        System.out.println("\n[Test Suite] 7. Custom Comparators & Sorting");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(30, "Zack Taylor", 60));
            service.addStudent(new Student(10, "Amy Pond", 95));
            service.addStudent(new Student(20, "Brian Cox", 80));

            // Sort by ID Ascending
            List<Student> byIdAsc = service.getSortedStudents(StudentComparators.BY_ID_ASC);
            assertTrue("Sort by ID Asc: first is 10", byIdAsc.get(0).getId() == 10);
            assertTrue("Sort by ID Asc: last is 30", byIdAsc.get(2).getId() == 30);

            // Sort by ID Descending
            List<Student> byIdDesc = service.getSortedStudents(StudentComparators.BY_ID_DESC);
            assertTrue("Sort by ID Desc: first is 30", byIdDesc.get(0).getId() == 30);

            // Sort by Name Ascending
            List<Student> byNameAsc = service.getSortedStudents(StudentComparators.BY_NAME_ASC);
            assertTrue("Sort by Name Asc: first is Amy", byNameAsc.get(0).getName().equals("Amy Pond"));
            assertTrue("Sort by Name Asc: last is Zack", byNameAsc.get(2).getName().equals("Zack Taylor"));

            // Sort by Marks Descending (highest first)
            List<Student> byMarksDesc = service.getSortedStudents(StudentComparators.BY_MARKS_DESC);
            assertTrue("Sort by Marks Desc: highest is Amy (95)", byMarksDesc.get(0).getMarks() == 95);
            assertTrue("Sort by Marks Desc: lowest is Zack (60)", byMarksDesc.get(2).getMarks() == 60);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testFilteringLogic() {
        System.out.println("\n[Test Suite] 8. Filtering Logic");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(1, "Passing Student", 75));
            service.addStudent(new Student(2, "Failing Student", 35));
            service.addStudent(new Student(3, "Top Student", 92));

            List<Student> passed = service.filterStudents(Student::isPassed);
            assertTrue("2 students passed", passed.size() == 2);

            List<Student> failed = service.filterStudents(s -> !s.isPassed());
            assertTrue("1 student failed", failed.size() == 1 && failed.get(0).getId() == 2);

            List<Student> distinction = service.filterStudents(s -> s.getMarks() >= 85);
            assertTrue("1 distinction student", distinction.size() == 1 && distinction.get(0).getId() == 3);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testInputValidation() {
        System.out.println("\n[Test Suite] 9. Input Validation & Exception Handling");

        // Test ID <= 0
        boolean invalidId = false;
        try {
            ValidationUtils.validateId(0);
        } catch (InvalidStudentDataException e) {
            invalidId = true;
        }
        assertTrue("Validation rejects zero ID", invalidId);

        // Test Negative ID
        invalidId = false;
        try {
            ValidationUtils.validateId(-5);
        } catch (InvalidStudentDataException e) {
            invalidId = true;
        }
        assertTrue("Validation rejects negative ID", invalidId);

        // Test Blank Name
        boolean invalidName = false;
        try {
            ValidationUtils.validateAndCleanName("   ");
        } catch (InvalidStudentDataException e) {
            invalidName = true;
        }
        assertTrue("Validation rejects empty/blank name", invalidName);

        // Test Name with symbols
        invalidName = false;
        try {
            ValidationUtils.validateAndCleanName("John@123");
        } catch (InvalidStudentDataException e) {
            invalidName = true;
        }
        assertTrue("Validation rejects symbols/numbers in name", invalidName);

        // Test Marks < 0
        boolean invalidMarks = false;
        try {
            ValidationUtils.validateMarks(-1);
        } catch (InvalidStudentDataException e) {
            invalidMarks = true;
        }
        assertTrue("Validation rejects negative marks", invalidMarks);

        // Test Marks > 100
        invalidMarks = false;
        try {
            ValidationUtils.validateMarks(105);
        } catch (InvalidStudentDataException e) {
            invalidMarks = true;
        }
        assertTrue("Validation rejects marks over 100", invalidMarks);

        // Test Boundary Marks 0 and 100
        try {
            ValidationUtils.validateMarks(0);
            ValidationUtils.validateMarks(100);
            assertTrue("Boundary marks 0 and 100 are valid", true);
        } catch (InvalidStudentDataException e) {
            assertTrue("Boundary marks failed validation", false);
        }
    }

    private static void testAnalyticsCalculation() {
        System.out.println("\n[Test Suite] 10. Analytics & Statistical Calculations");
        StudentService service = new StudentService();

        try {
            service.addStudent(new Student(1, "Student One", 80));
            service.addStudent(new Student(2, "Student Two", 90));
            service.addStudent(new Student(3, "Student Three", 70));

            double avg = service.getAverageMarks();
            assertTrue("Average marks is exactly 80.0", Math.abs(avg - 80.0) < 0.001);
            assertTrue("Highest scorer is Student Two (90)", service.getHighestScorer().getMarks() == 90);
            assertTrue("Lowest scorer is Student Three (70)", service.getLowestScorer().getMarks() == 70);
        } catch (Exception e) {
            assertTrue("Unexpected exception: " + e.getMessage(), false);
        }
    }

    private static void testFilePersistenceAndReload() {
        System.out.println("\n[Test Suite] 11. File Persistence and Restart Simulation");
        String testFile = "test_students_temp.txt";
        StudentService service1 = new StudentService();

        try {
            service1.addStudent(new Student(1001, "Grace Hopper", 98));
            service1.addStudent(new Student(1002, "Alan Turing", 99));
            service1.addStudent(new Student(1003, "Ada Lovelace", 100));

            // Save to file
            service1.saveToFile(testFile);

            // Simulate application restart with new service instance
            StudentService service2 = new StudentService();
            assertTrue("New service instance is initially empty", service2.getStudentCount() == 0);

            service2.loadFromFile(testFile);
            assertTrue("Reloaded service contains 3 students", service2.getStudentCount() == 3);

            Student alan = service2.searchById(1002);
            assertTrue("Alan Turing successfully restored with marks 99",
                    alan != null && alan.getName().equals("Alan Turing") && alan.getMarks() == 99);

            // Clean up temporary test file
            new File(testFile).delete();
            new File(testFile + ".tmp").delete();
        } catch (Exception e) {
            assertTrue("Persistence test failed: " + e.getMessage(), false);
        }
    }

    private static void testCorruptFileHandling() {
        System.out.println("\n[Test Suite] 12. Corrupt File Line Resilience");
        String corruptFile = "test_corrupt_data.txt";

        try {
            try (FileWriter writer = new FileWriter(corruptFile)) {
                writer.write("# Header comment\n");
                writer.write("1,Valid Student,85\n");
                writer.write("corrupted line without commas or numbers\n");
                writer.write("2,Second Valid,90\n");
                writer.write("3,Invalid Marks Student,999\n"); // Marks > 100
                writer.write("4,Another Valid,75\n");
            }

            StudentService service = new StudentService();
            service.loadFromFile(corruptFile);

            // Should safely parse the 3 valid rows (1, 2, 4) and skip the bad ones without crashing
            assertTrue("Parsed valid rows while safely skipping corrupt rows", service.getStudentCount() == 3);
            assertTrue("Student 1 loaded", service.searchById(1) != null);
            assertTrue("Student 2 loaded", service.searchById(2) != null);
            assertTrue("Student 4 loaded", service.searchById(4) != null);

            new File(corruptFile).delete();
        } catch (Exception e) {
            assertTrue("Corrupt file test failed: " + e.getMessage(), false);
        }
    }

    private static void testWrapperClassParsing() {
        System.out.println("\n[Test Suite] 13. Wrapper Classes & Safe Type Conversions");

        Integer parsedInt = ValidationUtils.parseIntegerSafe("  42  ");
        assertTrue("Parsed Integer 42 correctly", parsedInt.equals(42));

        Double parsedDouble = ValidationUtils.parseDoubleSafe("  98.6  ");
        assertTrue("Parsed Double 98.6 correctly", Math.abs(parsedDouble - 98.6) < 0.001);

        boolean nfeThrown = false;
        try {
            ValidationUtils.parseIntegerSafe("abc");
        } catch (NumberFormatException e) {
            nfeThrown = true;
        }
        assertTrue("NumberFormatException caught safely on invalid string", nfeThrown);
    }
}
