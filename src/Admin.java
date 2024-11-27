import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Admin extends User {

    public Admin(String username, String password, String jobType) {
        super(username, password, jobType);
    }

    public void addEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new employee name: ");
        String name = scanner.nextLine();

        System.out.println("Enter new employee password: ");
        String password = scanner.nextLine();

        String jobType = null;
        while (jobType == null) {
            System.out.println("Enter new employee job type (admin, hr, employee): ");
            String inputJobType = scanner.nextLine().toLowerCase();
            if ("admin".equals(inputJobType) || "hr".equals(inputJobType) || "employee".equals(inputJobType)) {
                jobType = inputJobType;
            } else {
                System.out.println("Invalid job type. Please enter 'admin', 'hr' or 'employee'.");
            }
        }

        // Append the new employee information to ValidLogins.csv
        try (FileWriter writer = new FileWriter("JavaModule/src/ValidLogins.csv", true)) {
            writer.append("\n");
            writer.append(name).append(",");
            writer.append(password).append(",");
            writer.append(jobType).append("\n");
            System.out.println("Employee added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Admin admin = new Admin("adminUsername", "adminPassword", "admin");
        admin.addEmployee();
    }

    //branch test
}