import java.util.Scanner;

public class HR extends User {

    public HR(String username, String password, String jobType) {
        super(username, password, jobType);
    }
    public void promoteEmployee() {
        Scanner input = new Scanner(System.in);
        Employee employee = null;

        // Prompt for Employee ID
        while (employee == null) {
            System.out.println("Enter the Employee ID to promote:");
            String empID = input.nextLine().trim();

            // Fetch the employee data
            employee = CSVManager.getEmployeeByID(empID);

            if (employee == null) {
                System.out.println("Employee not found. Please enter a valid Employee ID.");
            }
        }

        // Display current details
        System.out.println("Current Details:");
        System.out.println("Name: " + employee.getName());
        System.out.println("Job Title: " + employee.getJobTitle());
        System.out.println("Scale Point: " + employee.getScalePoint());

        // Save current job title and scale point as previous values
        employee.setPreviousJobTitle(employee.getJobTitle());
        employee.setPreviousScalePoint(employee.getScalePoint());

        // Prompt for new promotion details
        String newJobTitle = null;
        int newScalePoint = -1;

        // Validate job title
        while (newJobTitle == null || !CSVManager.isValidJobTitle(newJobTitle)) {
            System.out.println("Enter new job title (must be valid):");
            newJobTitle = input.nextLine().trim();
            if (!CSVManager.isValidJobTitle(newJobTitle)) {
                System.out.println("Invalid job title. Please try again.");
            }
        }

        // Validate scale point
        while (newScalePoint == -1 || !CSVManager.isValidScalePoint(newJobTitle, newScalePoint)) {
            System.out.println("Enter new scale point (must be within the valid range for the job title):");
            try {
                newScalePoint = Integer.parseInt(input.nextLine().trim());
                if (!CSVManager.isValidScalePoint(newJobTitle, newScalePoint)) {
                    System.out.println("Invalid scale point. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric scale point.");
            }
        }

        // Update the employee with new promotion details
        employee.setJobTitle(newJobTitle);
        employee.setScalePoint(newScalePoint);

        // Set promotion flag and update the CSV
        CSVManager.updateEmployeeDetails(employee, 1);

        System.out.println("Employee's role updated successfully. Awaiting confirmation.");
    }

}