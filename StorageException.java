/**
 * Thrown when file persistence operations fail (read, write, parsing, file access).
 */
public class StorageException extends StudentException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
