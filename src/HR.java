import java.util.Scanner;

public class HR extends User {

    public HR(String username, String password, String jobType) {
        super(username, password, jobType);
    }

    public void promoteEmployee() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the Employee ID to promote:");
        String empID = input.nextLine().trim();

        //Fetch employee data from CSV
        Employee employee = CSVManager.getEmployeeByID(empID);

        if (employee != null) {
            System.out.println("Current Details:");
            System.out.println("Name: " + employee.getName());
            System.out.println("Job Title: " + employee.getJobTitle());
            System.out.println("Scale Point: " + employee.getScalePoint());

            //Update job title
            System.out.println("Enter new job title:");
            String newJobTitle = input.nextLine().trim();

            //Update scale point
            System.out.println("Enter new scale point:");
            int newScalePoint = input.nextInt();
            input.nextLine(); // Consume the leftover newline

            //Update the employee details
            employee.setJobTitle(newJobTitle);
            employee.setScalePoint(newScalePoint);

            //Write updated details back to CSV
            CSVManager.updateEmployee(employee);

            System.out.println("Employee promoted successfully.");
        } else {
            System.out.println("Employee not found with ID: " + empID);
        }
    }
}