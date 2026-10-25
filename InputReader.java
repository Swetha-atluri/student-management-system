import java.util.Scanner;

/**
 * Reusable utility class for safe, interactive console inputs.
 * Handles spaces in strings, invalid number formats, empty inputs, and retries gracefully.
 */
public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads a required non-empty string line from the console.
     *
     * @param prompt Prompt message displayed to the user
     * @return Trimmed non-empty string
     */
    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line != null && !line.trim().isEmpty()) {
                return line.trim();
            }
            System.out.println("[!] Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads an optional string line from the console.
     *
     * @param prompt Prompt message displayed to the user
     * @return Trimmed string (may be empty if user pressed Enter)
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return line != null ? line.trim() : "";
    }

    /**
     * Reads a valid integer from the console.
     *
     * @param prompt Prompt message
     * @return Parsed Integer
     */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return ValidationUtils.parseIntegerSafe(line);
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid input. Please enter a valid whole number.");
            }
        }
    }

    /**
     * Reads an integer within a specified range [min, max].
     *
     * @param prompt Prompt message
     * @param min    Minimum acceptable value
     * @param max    Maximum acceptable value
     * @return Valid integer in range
     */
    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("[!] Value must be between " + min + " and " + max + ". Please try again.");
        }
    }

    /**
     * Prompts for confirmation (yes/no).
     *
     * @param prompt Prompt message
     * @return true if user answered 'y' or 'yes', false otherwise
     */
    public boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String line = scanner.nextLine();
            if (line != null) {
                String clean = line.trim().toLowerCase();
                if (clean.equals("y") || clean.equals("yes")) return true;
                if (clean.equals("n") || clean.equals("no")) return false;
            }
            System.out.println("[!] Please enter 'y' for yes or 'n' for no.");
        }
    }
}
