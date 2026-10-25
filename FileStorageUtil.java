import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for persistent file storage of student records.
 * Uses BufferedReader and BufferedWriter with FileWriter for safe and efficient I/O.
 */
public final class FileStorageUtil {

    private static final Logger LOGGER = Logger.getLogger(FileStorageUtil.class.getName());

    private FileStorageUtil() {
        // Prevent instantiation
    }

    /**
     * Saves a list of students to a text file in CSV format.
     * Uses a temporary file strategy to prevent data corruption during write failures.
     *
     * @param students List of students to save
     * @param filePath Target file path
     * @throws StorageException if an I/O error occurs
     */
    public static void saveStudents(List<Student> students, String filePath) throws StorageException {
        if (students == null) {
            throw new IllegalArgumentException("Student list cannot be null.");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        File targetFile = new File(filePath);
        File parentDir = targetFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        File tempFile = new File(filePath + ".tmp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            // Write CSV header
            writer.write("# ID,Name,Marks");
            writer.newLine();

            for (Student student : students) {
                if (student != null) {
                    writer.write(student.toCsv());
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed writing to temp file: " + tempFile.getAbsolutePath(), e);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            throw new StorageException("Failed to save student records to file: " + e.getMessage(), e);
        }

        // Atomically replace target file
        if (targetFile.exists() && !targetFile.delete()) {
            LOGGER.warning("Could not delete existing target file before replacement: " + targetFile.getName());
        }

        if (!tempFile.renameTo(targetFile)) {
            // Fallback: try copy if renameTo failed across file systems
            try (BufferedReader reader = new BufferedReader(new FileReader(tempFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new StorageException("Failed to copy temporary file to target destination: " + e.getMessage(), e);
            } finally {
                tempFile.delete();
            }
        }

        LOGGER.info("Successfully persisted " + students.size() + " student records to " + targetFile.getName());
    }

    /**
     * Loads student records from a text file.
     *
     * @param filePath Path to the file
     * @return List of parsed Student records
     * @throws StorageException if file reading fails
     */
    public static List<Student> loadStudents(String filePath) throws StorageException {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            LOGGER.info("Storage file not found: " + filePath + ". Returning empty list.");
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                // Skip empty lines or header/comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    Student student = Student.fromCsv(line);
                    // Validate read student data
                    ValidationUtils.validateId(student.getId());
                    ValidationUtils.validateAndCleanName(student.getName());
                    ValidationUtils.validateMarks(student.getMarks());
                    students.add(student);
                } catch (Exception ex) {
                    LOGGER.warning("Skipping malformed or invalid record at line " + lineNumber + ": '" + line + "'. Reason: " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed reading student records from file: " + filePath, e);
            throw new StorageException("Failed to read student records from file: " + e.getMessage(), e);
        }

        LOGGER.info("Successfully loaded " + students.size() + " valid student records from " + file.getName());
        return students;
    }
}
