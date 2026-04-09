import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        StudentService service = new StudentService();

        while (true) {

            System.out.println("\n1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();

                    System.out.print("Enter Name: ");
                    String name = sc.next();

                    System.out.print("Enter Marks: ");
                    int marks = sc.nextInt();

                    Student s = new Student(id, name, marks);
                    service.addStudent(s);
                    break;

                case 2:
                    service.viewStudents();
                    break;

                case 3:
                    System.out.print("Enter ID to update: ");
                    int uid = sc.nextInt();

                    System.out.print("Enter new name: ");
                    String newName = sc.next();

                    System.out.print("Enter new marks: ");
                    int newMarks = sc.nextInt();

                    service.updateStudent(uid, newName, newMarks);
                    break;

                case 4:
                    System.out.println("Thank you!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
