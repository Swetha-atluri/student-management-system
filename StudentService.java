import java.util.ArrayList;

public class StudentService {

    ArrayList<Student> studentList = new ArrayList<>();

    // Add student
    public void addStudent(Student s) {
        studentList.add(s);
        System.out.println("Student added successfully");
    }
    // Update student details
public void updateStudent(int id, String newName, int newMarks) {
    for (Student s : studentList) {
        if (s.getId() == id) {
            s.setName(newName);
            s.setMarks(newMarks);
            System.out.println("Student updated successfully");
            return;
        }
    }
    System.out.println("Student not found");
}


    // View all students
    public void viewStudents() {
        if (studentList.isEmpty()) {
            System.out.println("No students found");
        } else {
            for (Student s : studentList) {
                System.out.println(s);
            }
        }
    }
}
