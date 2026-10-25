import java.util.List;

/**
 * Utility class to format student collections and analytical summaries into clean, readable tabular formats.
 */
public final class TableFormatter {

    private TableFormatter() {
        // Prevent instantiation
    }

    /**
     * Formats a list of students into an ASCII table.
     *
     * @param students List of students
     * @return Formatted table string
     */
    public static String formatStudentTable(List<Student> students) {
        if (students == null || students.isEmpty()) {
            return "---------------------------------------------------------\n" +
                   "|                   No students to display               |\n" +
                   "---------------------------------------------------------";
        }

        StringBuilder sb = new StringBuilder();
        String line = "+-------+--------------------------------+-------+-------+----------+\n";
        String header = "| ID    | Name                           | Marks | Grade | Status   |\n";

        sb.append(line);
        sb.append(header);
        sb.append(line);

        for (Student s : students) {
            String status = s.isPassed() ? "Passed" : "Failed";
            String name = s.getName();
            if (name.length() > 30) {
                name = name.substring(0, 27) + "...";
            }
            sb.append(String.format("| %-5d | %-30s | %-5d | %-5s | %-8s |\n",
                    s.getId(), name, s.getMarks(), s.getGrade(), status));
        }

        sb.append(line);
        sb.append("Total Records: ").append(students.size()).append("\n");
        return sb.toString();
    }

    /**
     * Formats an individual student card.
     *
     * @param s Student
     * @return Card string
     */
    public static String formatStudentCard(Student s) {
        if (s == null) {
            return "No student data available.";
        }
        return "========================================\n" +
               " Student ID     : " + s.getId() + "\n" +
               " Full Name      : " + s.getName() + "\n" +
               " Marks Obtained : " + s.getMarks() + " / 100\n" +
               " Letter Grade   : " + s.getGrade() + "\n" +
               " Status         : " + (s.isPassed() ? "PASSED" : "FAILED") + "\n" +
               " Role           : " + s.getRoleDescription() + "\n" +
               "========================================";
    }

    /**
     * Formats performance analytics and statistics into a report.
     */
    public static String formatAnalyticsReport(int totalCount, double avgMarks, Student highest, Student lowest,
                                               int gradeA, int gradeB, int gradeC, int gradeD, int gradeF) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================================\n");
        sb.append("               STUDENT PERFORMANCE REPORT               \n");
        sb.append("=======================================================\n");
        sb.append(String.format(" Total Students Enrolled : %d\n", totalCount));
        if (totalCount > 0) {
            sb.append(String.format(" Class Average Marks     : %.2f / 100\n", avgMarks));
            if (highest != null) {
                sb.append(String.format(" Top Performer           : %s (ID: %d) with %d marks\n",
                        highest.getName(), highest.getId(), highest.getMarks()));
            }
            if (lowest != null) {
                sb.append(String.format(" Lowest Performer        : %s (ID: %d) with %d marks\n",
                        lowest.getName(), lowest.getId(), lowest.getMarks()));
            }
            sb.append("-------------------------------------------------------\n");
            sb.append(" Grade Breakdown:\n");
            sb.append(String.format("   - Grade A (85-100) : %d (%.1f%%)\n", gradeA, (gradeA * 100.0 / totalCount)));
            sb.append(String.format("   - Grade B (70-84)  : %d (%.1f%%)\n", gradeB, (gradeB * 100.0 / totalCount)));
            sb.append(String.format("   - Grade C (55-69)  : %d (%.1f%%)\n", gradeC, (gradeC * 100.0 / totalCount)));
            sb.append(String.format("   - Grade D (40-54)  : %d (%.1f%%)\n", gradeD, (gradeD * 100.0 / totalCount)));
            sb.append(String.format("   - Grade F (<40)    : %d (%.1f%%)\n", gradeF, (gradeF * 100.0 / totalCount)));
            int passed = gradeA + gradeB + gradeC + gradeD;
            sb.append(String.format(" Overall Pass Rate       : %.1f%%\n", (passed * 100.0 / totalCount)));
        } else {
            sb.append(" No student data available to compute analytics.\n");
        }
        sb.append("=======================================================\n");
        return sb.toString();
    }
}
