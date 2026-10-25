import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Student Management System.
 * Provides an interactive, robust menu-driven console interface.
 */
public class Main {

    private static final String DEFAULT_STORAGE_FILE = "students.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InputReader reader = new InputReader(sc);
        StudentService service = new StudentService();

        // Welcome banner
        System.out.println("=================================================");
        System.out.println("      WELCOME TO STUDENT MANAGEMENT SYSTEM       ");
        System.out.println("=================================================");

        // Automatically load existing records if default file exists
        try {
            service.loadFromFile(DEFAULT_STORAGE_FILE);
            if (service.getStudentCount() > 0) {
                System.out.println("[i] Automatically loaded " + service.getStudentCount() +
                        " record(s) from " + DEFAULT_STORAGE_FILE);
            }
        } catch (StorageException e) {
            System.out.println("[!] Notice: No existing records could be loaded (" + e.getMessage() + ")");
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = reader.readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleAddStudent(reader, service);
                    break;

                case 2:
                    handleViewStudents(service);
                    break;

                case 3:
                    handleUpdateStudent(reader, service);
                    break;

                case 4:
                    handleSearchStudent(reader, service);
                    break;

                case 5:
                    handleDeleteStudent(reader, service);
                    break;

                case 6:
                    handleSortStudents(reader, service);
                    break;

                case 7:
                    handleFilterStudents(reader, service);
                    break;

                case 8:
                    handleGenerateReport(service);
                    break;

                case 9:
                    handleSaveData(reader, service);
                    break;

                case 10:
                    handleReloadData(reader, service);
                    break;

                case 11:
                    running = handleExit(reader, service);
                    break;

                default:
                    System.out.println("Invalid choice. Please select an option from 1 to 11.");
            }
        }

        System.out.println("Thank you for using Student Management System!");
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. View All Students");
        System.out.println(" 3. Update Student");
        System.out.println(" 4. Search Student");
        System.out.println(" 5. Delete Student");
        System.out.println(" 6. Sort Students");
        System.out.println(" 7. Filter Students");
        System.out.println(" 8. Generate Student Report / Analytics");
        System.out.println(" 9. Save Data to File");
        System.out.println("10. Reload Data from File");
        System.out.println("11. Exit");
        System.out.println("---------------------------------------------");
    }

    private static void handleAddStudent(InputReader reader, StudentService service) {
        System.out.println("\n--- Add New Student ---");
        int id = reader.readInt("Enter ID: ");
        String name = reader.readNonEmptyString("Enter Name: ");
        int marks = reader.readInt("Enter Marks (0-100): ");

        try {
            Student s = new Student(id, name, marks);
            service.addStudent(s);
        } catch (DuplicateStudentException e) {
            System.out.println("[Error] " + e.getMessage());
        } catch (InvalidStudentDataException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Unexpected Error] " + e.getMessage());
        }
    }

    private static void handleViewStudents(StudentService service) {
        System.out.println("\n--- Enrolled Students ---");
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println(TableFormatter.formatStudentTable(students));
        }
    }

    private static void handleUpdateStudent(InputReader reader, StudentService service) {
        System.out.println("\n--- Update Student ---");
        int uid = reader.readInt("Enter ID to update: ");

        try {
            // Check existence first
            Student existing = service.searchById(uid);
            System.out.println("Current details: " + existing);

            String newName = reader.readNonEmptyString("Enter new name: ");
            int newMarks = reader.readInt("Enter new marks (0-100): ");

            service.updateStudent(uid, newName, newMarks);
        } catch (StudentNotFoundException e) {
            System.out.println("[Error] Student not found with ID " + uid);
        } catch (InvalidStudentDataException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Unexpected Error] " + e.getMessage());
        }
    }

    private static void handleSearchStudent(InputReader reader, StudentService service) {
        System.out.println("\n--- Search Student ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name (Substring / Case-Insensitive)");
        int subChoice = reader.readIntInRange("Choose search mode (1-2): ", 1, 2);

        if (subChoice == 1) {
            int sid = reader.readInt("Enter student ID to search: ");
            try {
                Student found = service.searchById(sid);
                System.out.println("\nMatch found:");
                System.out.println(TableFormatter.formatStudentCard(found));
            } catch (StudentNotFoundException e) {
                System.out.println("[!] No student found with ID: " + sid);
            } catch (InvalidStudentDataException e) {
                System.out.println("[Validation Error] " + e.getMessage());
            }
        } else {
            String query = reader.readNonEmptyString("Enter search keyword for name: ");
            List<Student> matches = service.searchByName(query);
            if (matches.isEmpty()) {
                System.out.println("[!] No students found matching: '" + query + "'");
            } else {
                System.out.println("\nFound " + matches.size() + " matching student(s):");
                System.out.println(TableFormatter.formatStudentTable(matches));
            }
        }
    }

    private static void handleDeleteStudent(InputReader reader, StudentService service) {
        System.out.println("\n--- Delete Student ---");
        int did = reader.readInt("Enter ID of student to delete: ");

        try {
            Student s = service.searchById(did);
            System.out.println("Record to remove: " + s);
            boolean confirm = reader.readConfirmation("Are you sure you want to delete this student?");
            if (confirm) {
                service.deleteStudent(did);
                System.out.println("[✓] Student with ID " + did + " deleted successfully.");
            } else {
                System.out.println("[i] Deletion cancelled.");
            }
        } catch (StudentNotFoundException e) {
            System.out.println("[Error] " + e.getMessage());
        } catch (InvalidStudentDataException e) {
            System.out.println("[Validation Error] " + e.getMessage());
        }
    }

    private static void handleSortStudents(InputReader reader, StudentService service) {
        System.out.println("\n--- Sort Students ---");
        System.out.println("1. Sort by ID (Ascending: 1 -> 999)");
        System.out.println("2. Sort by ID (Descending: 999 -> 1)");
        System.out.println("3. Sort by Name (Alphabetical: A -> Z)");
        System.out.println("4. Sort by Name (Reverse Alphabetical: Z -> A)");
        System.out.println("5. Sort by Marks (Highest to Lowest)");
        System.out.println("6. Sort by Marks (Lowest to Highest)");

        int choice = reader.readIntInRange("Select sorting order (1-6): ", 1, 6);
        List<Student> sorted;

        switch (choice) {
            case 1:
                sorted = service.getSortedStudents(StudentComparators.BY_ID_ASC);
                break;
            case 2:
                sorted = service.getSortedStudents(StudentComparators.BY_ID_DESC);
                break;
            case 3:
                sorted = service.getSortedStudents(StudentComparators.BY_NAME_ASC);
                break;
            case 4:
                sorted = service.getSortedStudents(StudentComparators.BY_NAME_DESC);
                break;
            case 5:
                sorted = service.getSortedStudents(StudentComparators.BY_MARKS_DESC);
                break;
            case 6:
                sorted = service.getSortedStudents(StudentComparators.BY_MARKS_ASC);
                break;
            default:
                sorted = service.getAllStudents();
        }

        System.out.println("\n--- Sorted Student List ---");
        System.out.println(TableFormatter.formatStudentTable(sorted));
    }

    private static void handleFilterStudents(InputReader reader, StudentService service) {
        System.out.println("\n--- Filter Students ---");
        System.out.println("1. Passed Students (Marks >= 40)");
        System.out.println("2. Failed Students (Marks < 40)");
        System.out.println("3. Distinction / Grade A (Marks >= 85)");
        System.out.println("4. Custom Marks Range (Min to Max)");

        int choice = reader.readIntInRange("Select filter option (1-4): ", 1, 4);
        List<Student> filtered;

        switch (choice) {
            case 1:
                filtered = service.filterStudents(Student::isPassed);
                break;
            case 2:
                filtered = service.filterStudents(s -> !s.isPassed());
                break;
            case 3:
                filtered = service.filterStudents(s -> s.getMarks() >= 85);
                break;
            case 4:
                int min = reader.readIntInRange("Enter minimum marks (0-100): ", 0, 100);
                int max = reader.readIntInRange("Enter maximum marks (" + min + "-100): ", min, 100);
                filtered = service.filterStudents(s -> s.getMarks() >= min && s.getMarks() <= max);
                break;
            default:
                filtered = service.getAllStudents();
        }

        System.out.println("\n--- Filtered Results (" + filtered.size() + " matches) ---");
        System.out.println(TableFormatter.formatStudentTable(filtered));
    }

    private static void handleGenerateReport(StudentService service) {
        List<Student> all = service.getAllStudents();
        int total = all.size();
        double avg = service.getAverageMarks();
        Student highest = service.getHighestScorer();
        Student lowest = service.getLowestScorer();

        int gradeA = service.filterStudents(s -> "A".equals(s.getGrade())).size();
        int gradeB = service.filterStudents(s -> "B".equals(s.getGrade())).size();
        int gradeC = service.filterStudents(s -> "C".equals(s.getGrade())).size();
        int gradeD = service.filterStudents(s -> "D".equals(s.getGrade())).size();
        int gradeF = service.filterStudents(s -> "F".equals(s.getGrade())).size();

        String report = TableFormatter.formatAnalyticsReport(total, avg, highest, lowest,
                gradeA, gradeB, gradeC, gradeD, gradeF);
        System.out.println(report);
    }

    private static void handleSaveData(InputReader reader, StudentService service) {
        System.out.println("\n--- Save Student Records ---");
        String path = reader.readString("Enter file name/path (Press Enter for default: '" + DEFAULT_STORAGE_FILE + "'): ");
        if (path.isEmpty()) {
            path = DEFAULT_STORAGE_FILE;
        }

        try {
            service.saveToFile(path);
            System.out.println("[✓] Successfully saved " + service.getStudentCount() + " records to: " + path);
        } catch (StorageException e) {
            System.out.println("[Error] Failed to save data: " + e.getMessage());
        }
    }

    private static void handleReloadData(InputReader reader, StudentService service) {
        System.out.println("\n--- Reload Student Records ---");
        String path = reader.readString("Enter file name/path to load (Press Enter for default: '" + DEFAULT_STORAGE_FILE + "'): ");
        if (path.isEmpty()) {
            path = DEFAULT_STORAGE_FILE;
        }

        boolean confirm = true;
        if (service.getStudentCount() > 0) {
            confirm = reader.readConfirmation("Reloading will replace current unsaved records in memory. Continue?");
        }

        if (confirm) {
            try {
                service.loadFromFile(path);
                System.out.println("[✓] Loaded " + service.getStudentCount() + " record(s) from: " + path);
            } catch (StorageException e) {
                System.out.println("[Error] Could not load records: " + e.getMessage());
            }
        } else {
            System.out.println("[i] Reload operation cancelled.");
        }
    }

    private static boolean handleExit(InputReader reader, StudentService service) {
        if (service.getStudentCount() > 0) {
            boolean save = reader.readConfirmation("Do you want to save your changes to '" + DEFAULT_STORAGE_FILE + "' before exiting?");
            if (save) {
                try {
                    service.saveToFile(DEFAULT_STORAGE_FILE);
                    System.out.println("[✓] Records saved successfully.");
                } catch (StorageException e) {
                    System.out.println("[!] Warning: Could not auto-save on exit: " + e.getMessage());
                }
            }
        }
        return false;
    }
}
